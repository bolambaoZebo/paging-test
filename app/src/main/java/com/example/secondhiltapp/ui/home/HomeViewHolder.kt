package com.example.secondhiltapp.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ItemSoccerNewsBinding
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.preferences.SortOrder
import com.example.secondhiltapp.utils.setSafeOnClickListener

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
            if (language == "zh") {
                binding.itemHorseNewsTitle.text = video.titleChinese
            } else {
                binding.itemHorseNewsTitle.text = video.title
            }
            Glide.with(itemView)
                .load(video.imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_sports_soccer_24)
                .into(itemHorseNewsImage)

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

            itemHorseNewsImage.setOnClickListener {
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
//            itemHorseNewsImage.setSafeOnClickListener {
//                it.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onItemClick(position)
//                    }
//                }
//            }
//
//
//            savePost.setSafeOnClickListener {
//                it.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onBookmarkClick(position)
//                    }
//                }
//
//            }
//
//            likePost.setSafeOnClickListener {
//                it.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onLikeClick(position)
//                    }
//                }
//            }
//
//            commentsPost.setSafeOnClickListener {
//                it.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onCommentClick(position)
//                    }
//                }
//            }

        }
    }
}


//            itemHorseNewsImage.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    onItemClick(position)
//                }
//                savePost.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onBookmarkClick(position)
//                    }
//                }
//                likePost.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onLikeClick(position)
//                    }
//                }
//                commentsPost.setOnClickListener {
//                    val position = bindingAdapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        onCommentClick(position)
//                    }
//                }