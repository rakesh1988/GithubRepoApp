package com.example.github.repositories.presentation.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.github.repositories.R
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater).apply {
            title.text = args.repositoryItem.name
            detail.text =
                "Created by " + args.repositoryItem.owner!!.login + ", at " + args.repositoryItem.created_at
            Picasso.get().load(args.repositoryItem.owner!!.avatar_url).into(image)
            description.text = args.repositoryItem.description
            url.text = args.repositoryItem.html_url
            image.setImageResource(
                if (LocalDataStore.instance.getBookmarks().contains(args.repositoryItem))
                    R.drawable.baseline_bookmark_black_24
                else
                    R.drawable.baseline_bookmark_border_black_24
            )
            image.setOnClickListener {
                val isBookmarked =
                    LocalDataStore.instance.getBookmarks().contains(args.repositoryItem)
                LocalDataStore.instance.bookmarkRepo(args.repositoryItem, !isBookmarked)
                image.setImageResource(if (!isBookmarked) R.drawable.baseline_bookmark_black_24 else R.drawable.baseline_bookmark_border_black_24)
            }
            detail.setOnClickListener {
                args.repositoryItem.owner?.let {
                    val action = DetailFragmentDirections.actionDetailFragmentToUserFragment(it)
                    findNavController().navigate(action)
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}