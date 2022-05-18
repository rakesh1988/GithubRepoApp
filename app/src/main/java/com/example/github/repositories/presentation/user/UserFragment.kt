package com.example.github.repositories.presentation.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.github.repositories.RepositoryAdapter
import com.example.github.repositories.data.remotemodel.OwnerDTO
import com.example.github.repositories.databinding.FragmentUserBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment(private val user: OwnerDTO) : Fragment() {

    private val viewModel: UserViewModel by viewModels()

    private var binding: FragmentUserBinding? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater).apply {
            title.text = user.login
            Picasso.get().load(user.avatar_url.toUri()).into(image)
        }

        viewModel.fetchUser(user.login)
        viewModel.user.observe(viewLifecycleOwner) {
            binding!!.detail.text = "Twitter handle: " + it.twitter_username
            viewModel.fetchRepositories(it.repos_url!!)
        }
        viewModel.repositories.observe(viewLifecycleOwner) {
            binding!!.list.adapter = RepositoryAdapter(it.toMutableList(), requireActivity())
        }
        return binding!!.root
    }
}