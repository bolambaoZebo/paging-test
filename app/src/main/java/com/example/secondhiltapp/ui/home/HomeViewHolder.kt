package com.example.secondhiltapp.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ItemSoccerNewsBinding
import com.example.secondhiltapp.db.entity.SoccerNews

class HomeViewHolder(
    private val binding: ItemSoccerNewsBinding,
    private val onItemClick: (Int) -> Unit,
    private val onLikeClick: (Int) -> Unit,
    private val onCommentClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit,
    private var language: String
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(video: SoccerNews) {
        binding.apply {
            if (language == "en") {
                binding.itemHorseNewsTitle.text = video.title
                binding.itemHorsNewsDescription.text = video.description
                Glide.with(itemView).load(video.imageUrl).into(itemHorseNewsImage)
            }

            if (language == "zh") {
                binding.itemHorseNewsTitle.text = video.titleChinese
                binding.itemHorsNewsDescription.text = video.descriptionChinese
                Glide.with(itemView).load(video.imageUrl).into(itemHorseNewsImage)
            }

//            textViewTitle.text = article.title ?: ""
//            imageViewBookmark.setImageResource(
//                when {
//                    article.isBookmarked -> R.drawable.ic_bookmark_selected
//                    else -> R.drawable.ic_bookmark_unselected
//                }
//            )
        }
    }

    init {
        binding.apply {
            root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
            savePost.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookmarkClick(position)
                }
            }
            likePost.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLikeClick(position)
                }
            }
            commentsPost.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCommentClick(position)
                }
            }

        }
    }
}