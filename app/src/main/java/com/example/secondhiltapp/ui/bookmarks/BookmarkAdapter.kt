package com.example.secondhiltapp.ui.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ItemBookmarkBinding
import com.example.secondhiltapp.db.entity.BookMarkData

class BookmarkAdapter : ListAdapter<BookMarkData, BookmarkAdapter.BookMarkViewHolder>(DiffCallback()) {

    class BookMarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(bookmark: BookMarkData){
            binding.apply {
                bookmarkItemTitle.text = bookmark.title
                bookmarkItemDescription.text = bookmark.description

                Glide.with(itemView)
                    .load(bookmark.imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(bookmarkItemImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookMarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<BookMarkData>(){
        override fun areItemsTheSame(oldItem: BookMarkData, newItem: BookMarkData) =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: BookMarkData, newItem: BookMarkData) =
            oldItem == newItem

    }
}