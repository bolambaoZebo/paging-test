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

    private var imageList = emptyList<String>()

    inner class HomeSliderViewHolder(private val binding: ItemHomeSliderBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: String){
            binding.apply {
                Glide.with(itemView)
                    .load(data)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_baseline_sports_soccer_24)
                    .placeholder(R.drawable.ic_baseline_image_search_24)
                    .into(homeItemImageSlider)

                homeItemImageSlider.setOnClickListener {
                    listener.onSliderImageClicked(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSliderViewHolder {
        val binding = ItemHomeSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeSliderViewHolder, position: Int) {
        val currentItem = imageList?.get(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount() = imageList.size

    fun setupList(newList: List<String>){
        imageList = newList
        notifyDataSetChanged()
    }

    interface OnItemSliderClick {
        fun onSliderImageClicked(imageUrl: String)
    }
}