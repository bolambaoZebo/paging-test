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
import com.example.secondhiltapp.preferences.SortOrder
import com.example.secondhiltapp.utils.LOCAL_ENGLISH
import com.example.secondhiltapp.utils.setSafeOnClickListener

class BookmarkAdapter(
    private val listener: OnItemBookmarkClick
) : ListAdapter<BookMarkData, BookmarkAdapter.BookMarkViewHolder>(DiffCallback()) {

    private var language = "en"

    inner class BookMarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(bookmark: BookMarkData){
            binding.apply {

                if (language == LOCAL_ENGLISH){
                    bookmarkItemTitle.text = bookmark.title
                    bookmarkItemDescription.text = bookmark.description
                }else{
                    bookmarkItemTitle.text = if (bookmark.type == SortOrder.BY_NEWS) bookmark.titleChinese else bookmark.title
                    bookmarkItemDescription.text = if (bookmark.type == SortOrder.BY_NEWS) bookmark.descriptionChinese else bookmark.description
                }

                Glide.with(itemView)
                    .load(bookmark.imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(bookmarkItemImage)

                rootItemBookmark.setSafeOnClickListener {
                    listener.onItemClick(bookmark, language)
                }
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

    fun setLanguagesTo(lang: String){
        language = lang
        notifyDataSetChanged()
    }

    fun getLanguage() : String {
        return language
    }

    interface OnItemBookmarkClick {
        fun onItemClick(bookmark: BookMarkData, lang: String)
    }

}