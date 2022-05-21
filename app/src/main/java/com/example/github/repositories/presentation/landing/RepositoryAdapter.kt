package com.example.github.repositories.presentation.landing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.R
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.presentation.shared.RecyclerViewItemClickListener
import java.util.*

class RepositoryAdapter(
    val list: List<RepositoryDTO>,
    private val onClickListener: RecyclerViewItemClickListener
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData()
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val container: View = itemView.findViewById(R.id.news_container)
        val titleTxt: TextView = itemView.findViewById(R.id.title)
        val imageVw: ImageView = itemView.findViewById(R.id.image)
        val descriptionTxt: TextView = itemView.findViewById(R.id.description)
        val authorTxt: TextView = itemView.findViewById(R.id.author)

        @SuppressLint("SetTextI18n")
        fun bindData() {
            val item = list[adapterPosition]
            titleTxt.text =
                "#" + (adapterPosition + 1) + ": " + item.full_name?.uppercase(Locale.getDefault())
            descriptionTxt.text =
                if (item.description.orEmpty().length > 150) item.description?.take(150)
                    .plus("...") else item.description
            authorTxt.text = item.owner?.login
            imageVw.setImageResource(
                if (LocalDataStore.instance.getBookmarks().contains(item))
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
}