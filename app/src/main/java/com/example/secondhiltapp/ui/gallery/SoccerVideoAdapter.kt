package com.example.secondhiltapp.ui.gallery

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.databinding.ItemUnsplashPhotoBinding
import com.example.secondhiltapp.utils.convertToCustomFormat
import com.example.secondhiltapp.utils.setSafeOnClickListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SoccerVideoAdapter(
    private val onItemImage: (SoccerVideos) -> Unit,
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
            binding.imageOnclick.setSafeOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
//                        listener.onItemClicked(item)
                        onItemImage(item)
                    }
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(video: SoccerVideos) {
            binding.apply {
                Glide.with(itemView)
                    .load(video.thumbnail)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_baseline_sports_soccer_24)
                    .into(imageView)

                textViewUserName.text = video.title

                txtVideoDate.text = convertToCustomFormat(video.date).uppercase()//date.toString()

                saveHighlights.setSafeOnClickListener {
                    onBookmarkClick(video)
                }


            }
        }
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


//                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssss")
//                val date: Date = format.parse(video.date)

//    fun convertISOTimeToDate(isoTime: String): String? {
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//        var convertedDate: Date? = null
//        var formattedDate: String? = null
//        try {
//            convertedDate = sdf.parse(isoTime)
//            formattedDate = SimpleDateFormat("dd-MM-yyyy" + "\n" + " hh:mm:ss a").format(convertedDate)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        return formattedDate
//    }
}