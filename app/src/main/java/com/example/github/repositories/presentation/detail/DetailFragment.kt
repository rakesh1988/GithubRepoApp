package com.example.github.repositories.presentation.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.github.repositories.R
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.databinding.FragmentDetailBinding
import com.example.github.repositories.presentation.user.UserFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment(private val repository: RepositoryDTO) : Fragment() {

    private var binding: FragmentDetailBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater).apply {
            title.text = repository.name
            detail.text = "Created by " + repository.owner!!.login + ", at " + repository.created_at
            Picasso.get().load(repository.owner!!.avatar_url).into(image)
            description.text = repository.description
            url.text = repository.html_url
            image.setImageResource(
                if (LocalDataStore.instance.getBookmarks().contains(repository))
                    R.drawable.baseline_bookmark_black_24
                else
                    R.drawable.baseline_bookmark_border_black_24
            )
            image.setOnClickListener {
                val isBookmarked = LocalDataStore.instance.getBookmarks().contains(repository)
                LocalDataStore.instance.bookmarkRepo(repository, !isBookmarked)
                image.setImageResource(if (!isBookmarked) R.drawable.baseline_bookmark_black_24 else R.drawable.baseline_bookmark_border_black_24)
            }
            detail.setOnClickListener {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, UserFragment(repository.owner!!))
                    .addToBackStack("user")
                    .commit()
            }
        }


        return binding!!.root
    }
}