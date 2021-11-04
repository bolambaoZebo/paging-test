package com.example.secondhiltapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.secondhiltapp.databinding.ItemSoccerNewsBinding
import com.example.secondhiltapp.db.entity.SoccerNews

class HomeAdapter (
    private val onItemClick: (SoccerNews) -> Unit,
    private val onLikeClick: (SoccerNews) -> Unit,
    private val onCommentClick: (SoccerNews) -> Unit,
    private val onBookmarkClick: (SoccerNews) -> Unit
) : ListAdapter<SoccerNews, HomeViewHolder>(HomeComparator()) {

    private var language = "en"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemSoccerNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding,
            onItemClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onItemClick(article)
                }
            },
            onLikeClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onLikeClick(article)
                }
            },
            onCommentClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onCommentClick(article)
                }
            },
            onBookmarkClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onBookmarkClick(article)
                }
            },
            language
        )
    }

    fun setLanguagesTo(lang: String){
        language = lang
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}