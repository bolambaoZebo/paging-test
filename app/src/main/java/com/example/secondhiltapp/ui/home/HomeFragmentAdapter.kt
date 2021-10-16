package com.example.secondhiltapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhiltapp.databinding.HomeFragmentBinding
import com.example.secondhiltapp.databinding.ItemSoccerNewsBinding
import com.example.secondhiltapp.db.entity.SoccerNews

class HomeFragmentAdapter(
    private val context: Context,
    private val listener: OnClickListeners
) : RecyclerView.Adapter<HomeFragmentAdapter.HomeViewHolder>() {

    private var oldData = emptyList<SoccerNews>()
    private var language = "en"

    inner class HomeViewHolder(val binding: ItemSoccerNewsBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(data: SoccerNews){
            binding.apply {
                if (language == "en") {
                    binding.itemHorseNewsTitle.text = data.title
                    binding.itemHorsNewsDescription.text = data.description
                    Glide.with(context).load(data.imageUrl).into(binding.itemHorseNewsImage)
                }

                if (language == "zh") {
                    binding.itemHorseNewsTitle.text = data.titleChinese
                    binding.itemHorsNewsDescription.text = data.descriptionChinese
                    Glide.with(context).load(data.imageUrl).into(binding.itemHorseNewsImage)
                }

                savePost.setOnClickListener {
                    listener.onSaveClick(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(ItemSoccerNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = oldData?.get(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return oldData.size
    }

    fun setLanguagesTo(lang: String){
        language = lang
        notifyDataSetChanged()
    }

    fun setHomeData(newData: List<SoccerNews>){
        oldData = newData
        notifyDataSetChanged()
    }

    interface OnClickListeners {
        fun onSaveClick(data: SoccerNews)
    }
}