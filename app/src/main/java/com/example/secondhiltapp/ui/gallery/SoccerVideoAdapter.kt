package com.example.secondhiltapp.ui.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.databinding.ItemUnsplashPhotoBinding
import com.example.secondhiltapp.db.entity.BookMarkData

class SoccerVideoAdapter(
    private val listener: OnItemClickListener,
    private val onBookmarkClick: (SoccerVideos) -> Unit
) :
    PagingDataAdapter<SoccerVideos, SoccerVideoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {
    // PagingDataAdapter<UnsplashPhoto, SoccerVideoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class PhotoViewHolder(
        private val binding: ItemUnsplashPhotoBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageOnclick.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClicked(item)
                    }
                }
            }
        }

        fun bind(video: SoccerVideos) {
            binding.apply {
                Glide.with(itemView)
                    .load(video.thumbnail)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                textViewUserName.text = video.title

                saveHighlights.setOnClickListener {
                    onBookmarkClick(video)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(video: SoccerVideos)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<SoccerVideos>() {
            override fun areItemsTheSame(oldItem: SoccerVideos, newItem: SoccerVideos) =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SoccerVideos, newItem: SoccerVideos) =
                oldItem == newItem
        }
    }
}