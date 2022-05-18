package com.example.github.repositories.presentation.landing

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.github.repositories.RepositoryAdapter
import com.example.github.repositories.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

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
            val adapter = RepositoryAdapter(it.take(20).toMutableList(), requireActivity())
            binding?.repoList?.adapter = adapter
        }
        return binding!!.root
    }
}