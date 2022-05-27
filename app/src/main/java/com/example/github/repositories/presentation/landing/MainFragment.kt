package com.example.github.repositories.presentation.landing

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.databinding.FragmentMainBinding
import com.example.github.repositories.presentation.shared.RecyclerViewItemClickListener
import com.example.github.repositories.shared.getLogTag
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val TAG = getLogTag()

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater).apply {
            swipeRefresh.setOnRefreshListener {
                startLoading()
                viewModel.refresh()
            }
        }

        viewModel.repositories.observe(viewLifecycleOwner) {
            val adapter = RepositoryAdapter(it.take(20).toMutableList(),
                RepositoryAdapter.ItemType.VIEW_TYPE_DATA,
                object : RecyclerViewItemClickListener {
                    override fun onClick(item: Any, position: Int) {
                        Log.d(TAG, "clicked on item number $position")
                        val action =
                            MainFragmentDirections.actionMainFragmentToDetailFragment(item as RepositoryDTO)
                        findNavController()
                            .navigate(action)
                    }
                }
            )
            binding.repoList.adapter = adapter
            binding.swipeRefresh.isRefreshing = false // hide refresh icon
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
                        viewModel.refresh()
                    }
                }
            )
            binding.repoList.adapter = adapter
            binding.swipeRefresh.isRefreshing = false // hide refresh icon
            stopLoading()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.repoList.adapter?.notifyDataSetChanged()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}