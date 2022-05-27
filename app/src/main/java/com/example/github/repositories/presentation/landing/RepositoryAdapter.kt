package com.example.github.repositories.presentation.landing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.R
import com.example.github.repositories.data.BookmarkDataStore
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.presentation.landing.RepositoryAdapter.ItemType.*
import com.example.github.repositories.presentation.shared.RecyclerViewItemClickListener

class RepositoryAdapter(
    val list: List<RepositoryDTO>,
    private val itemType: ItemType,
    private val onClickListener: RecyclerViewItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ItemType {
        VIEW_TYPE_EMPTY,
        VIEW_TYPE_RETRY,
        VIEW_TYPE_DATA
    }

    override fun getItemViewType(position: Int): Int {
        return itemType.ordinal
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty())
            1 // returning 1 because we must show either retry or no item here
        else
            list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
                DataTypeViewHolder(view)
            }
            VIEW_TYPE_EMPTY.ordinal -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
                EmptyTypeViewHolder(view)
            }
            VIEW_TYPE_RETRY.ordinal -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_retry, parent, false)
                RetryTypeViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
                EmptyTypeViewHolder(view)
            }
        }
    }

    inner class EmptyTypeViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        // do something if you want for empty type
    }

    inner class RetryTypeViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val retryButton: Button = itemView.findViewById(R.id.retry_button)
        fun bindData() {
            retryButton.setOnClickListener {
                onClickListener.onClick(Unit, adapterPosition)
            }
        }

    }


    inner class DataTypeViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val container: View = itemView.findViewById(R.id.news_container)
        private val titleTxt: TextView = itemView.findViewById(R.id.title)
        private val imageVw: ImageView = itemView.findViewById(R.id.image)
        private val descriptionTxt: TextView = itemView.findViewById(R.id.description)
        private val authorTxt: TextView = itemView.findViewById(R.id.author)

        @SuppressLint("SetTextI18n")
        fun bindData() {
            val item = list[adapterPosition]
            val itemDto =
                RepositoryTransformer().transformRepoListToRepoItemDto(item, adapterPosition)
            titleTxt.text = itemDto.title
            descriptionTxt.text = itemDto.description
            authorTxt.text = itemDto.author
            imageVw.setImageResource(
                if (BookmarkDataStore.isRepoBookmarked(item.id))
                    R.drawable.baseline_bookmark_black_24
                else
                    R.drawable.baseline_bookmark_border_black_24
            )
            container.setOnClickListener {
                onClickListener.onClick(item, adapterPosition)
                // its not a good idea to pass activity into adapter
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_DATA.ordinal -> {
                (holder as DataTypeViewHolder).bindData()
            }
            VIEW_TYPE_RETRY.ordinal -> {
                (holder as RetryTypeViewHolder).bindData()
            }
        }
    }
}