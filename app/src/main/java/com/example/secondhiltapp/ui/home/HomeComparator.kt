package com.example.secondhiltapp.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.example.secondhiltapp.db.entity.SoccerNews

class HomeComparator: DiffUtil.ItemCallback<SoccerNews>() {

    override fun areItemsTheSame(oldItem: SoccerNews, newItem: SoccerNews) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SoccerNews, newItem: SoccerNews) =
        oldItem == newItem
}