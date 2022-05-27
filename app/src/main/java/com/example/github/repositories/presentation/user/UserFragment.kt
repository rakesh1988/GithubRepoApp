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

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val args: UserFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater).apply {
            title.text = args.ownerItem.login
            Picasso.get().load(args.ownerItem.avatar_url?.toUri()).into(image)
        }
        viewModel.viewModelInitComplete.observe(viewLifecycleOwner) {
            viewModel.fetchUser(args.ownerItem.login)
        }
        viewModel.user.observe(viewLifecycleOwner) { userDto ->
            userDto?.let {
                binding.detail.text = "Twitter handle:  ${it.twitter_username ?: "NA"}"
                viewModel.fetchRepositories(it.repos_url!!)
            }
        }

        viewModel.repositories.observe(viewLifecycleOwner) {
            binding.repoList.adapter = RepositoryAdapter(
                it.toMutableList(),
                RepositoryAdapter.ItemType.VIEW_TYPE_DATA,
                object : RecyclerViewItemClickListener {
                    override fun onClick(item: Any, position: Int) {
                        Log.d(TAG, "clicked on item number $position")
                        val action =
                            UserFragmentDirections.actionUserFragmentToDetailFragment(item as RepositoryDTO)
                        findNavController()
                            .navigate(action)
                    }
                }
            )
            stopLoading()
        }

        viewModel.errorFetchingData.observe(viewLifecycleOwner) {
            val adapter = RepositoryAdapter(
                emptyList(),
                RepositoryAdapter.ItemType.VIEW_TYPE_RETRY,
                object : RecyclerViewItemClickListener {
                    override fun onClick(item: Any, position: Int) {
                        Log.d(TAG, "clicked on retry")
                        startLoading()
                        viewModel.fetchUser(args.ownerItem.login)
                    }
                }
            )
            binding.repoList.adapter = adapter
            stopLoading()
        }
        return binding.root
    }

    private fun startLoading() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.repoList.visibility = View.GONE
        binding.shimmerFrameLayout.startShimmer()
    }

    private fun stopLoading() {
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.repoList.visibility = View.VISIBLE
        binding.shimmerFrameLayout.stopShimmer()
    }

}