package com.example.github.repositories.presentation.shared

class RecyclerViewItemClickListener(val clickListener: (item: Any, position: Int) -> Unit) {
    fun onClick(item: Any, position: Int) = clickListener(item, position)
}
