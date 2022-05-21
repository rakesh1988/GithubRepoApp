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

    private var binding: FragmentMainBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater).apply {
            swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        }
        viewModel.fetchItems()

        viewModel.repositories.observe(viewLifecycleOwner) {
            val adapter = RepositoryAdapter(it.take(20).toMutableList(),
                RecyclerViewItemClickListener { item, position ->
                    Log.d(TAG, "clicked on item number $position")
                    val action =
                        MainFragmentDirections.actionMainFragmentToDetailFragment(item as RepositoryDTO)
                    findNavController()
                        .navigate(action)
                }
            )
            binding?.repoList?.adapter = adapter
            binding?.swipeRefresh?.isRefreshing = false // hide refresh icon
        }
        return binding!!.root
    }
}