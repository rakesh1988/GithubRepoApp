package com.example.github.repositories.presentation.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.databinding.FragmentUserBinding
import com.example.github.repositories.presentation.landing.RepositoryAdapter
import com.example.github.repositories.presentation.shared.RecyclerViewItemClickListener
import com.example.github.repositories.shared.getLogTag
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private val TAG = getLogTag()

    private val viewModel: UserViewModel by viewModels()

    private var binding: FragmentUserBinding? = null

    private val args: UserFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater).apply {
            title.text = args.ownerItem.login
            Picasso.get().load(args.ownerItem.avatar_url?.toUri()).into(image)
        }

        viewModel.fetchUser(args.ownerItem.login)
        viewModel.user.observe(viewLifecycleOwner) {
            binding!!.detail.text = "Twitter handle: " + it.twitter_username
            viewModel.fetchRepositories(it.repos_url!!)
        }
        viewModel.repositories.observe(viewLifecycleOwner) {
            binding!!.list.adapter = RepositoryAdapter(
                it.toMutableList(),
                RecyclerViewItemClickListener { item, position ->
                    Log.d(TAG, "clicked on item number $position")
                    val action =
                        UserFragmentDirections.actionUserFragmentToDetailFragment(item as RepositoryDTO)
                    findNavController()
                        .navigate(action)
                }
            )
        }
        return binding!!.root
    }
}