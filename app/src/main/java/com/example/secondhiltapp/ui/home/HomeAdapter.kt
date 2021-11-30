package com.example.secondhiltapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.secondhiltapp.databinding.ItemSoccerNewsBinding
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.utils.getLocale

class HomeAdapter (
    private val onItemClick: (SoccerNews, lang: String) -> Unit,
    private val onLikeClick: (SoccerNews) -> Unit,
    private val onCommentClick: (SoccerNews) -> Unit,
    private val onBookmarkClick: (SoccerNews, lang: String) -> Unit,
    private val context: Context
) : ListAdapter<SoccerNews, HomeViewHolder>(HomeComparator()) {

    private val currentLocale = getLocale(context.resources)
    private var language = currentLocale.toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemSoccerNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding,
            onItemClick = { position ->
                val article = getItem(position)
                if (article != null) {
                    onItemClick(article, language)
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
                    onBookmarkClick(article, language)
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