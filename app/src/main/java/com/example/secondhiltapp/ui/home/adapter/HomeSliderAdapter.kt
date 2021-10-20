package com.example.secondhiltapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ItemHomeSliderBinding
import com.example.secondhiltapp.db.entity.SoccerNews

class HomeSliderAdapter(
    private val listener: OnItemSliderClick
) : RecyclerView.Adapter<HomeSliderAdapter.HomeSliderViewHolder>(){

//    private var imageList = emptyList<String>()

    private var imageList = emptyList<SoccerNews>()

    inner class HomeSliderViewHolder(private val binding: ItemHomeSliderBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(data: String, position: Int){
        fun bind(data: SoccerNews, position: Int){
            binding.apply {
                Glide.with(itemView)
                    .load(data.imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(homeItemImageSlider)

                homeItemImageSlider.setOnClickListener {
                    listener.onSliderImageClicked(data.imageUrl.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSliderViewHolder {
        val binding = ItemHomeSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeSliderViewHolder, position: Int) {
//        holder.bind(imageList[position], position)
        val currentItem = imageList?.get(position)
        if (currentItem != null) {
            holder.bind(currentItem, position)
        }
    }

    override fun getItemCount() = imageList.size

    fun setupList(newList: List<SoccerNews>){
        imageList = newList
        notifyDataSetChanged()
    }

    fun getImageSize() : Int {
        return imageList.size
    }

    fun getIndices() : IntRange {
        return 0..5
    }

    interface OnItemSliderClick {
        fun onSliderImageClicked(imageUrl: String)
    }
}