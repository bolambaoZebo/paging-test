package com.example.secondhiltapp.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.LOCAL_ENGLISH
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.FragmentDetailsNewsBinding
import com.example.secondhiltapp.db.entity.BookMarkData


class NewsDetailsFragment(
    private val bookMarkData: BookMarkData,
    val l: String
) : Fragment(R.layout.fragment_details_news) {

    private val args by navArgs<NewsDetailsFragmentArgs>()

    private var _binding: FragmentDetailsNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsNewsBinding.bind(view)

        binding.apply {
            Glide.with(this@NewsDetailsFragment)
                .load(bookMarkData.imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(newsDetailsImage)

            newsTitle.text = if (l == LOCAL_ENGLISH)bookMarkData.title!! else bookMarkData.titleChinese!!//args.newsText
            newsDescription.text =  if (l == LOCAL_ENGLISH)bookMarkData.description!! else bookMarkData.descriptionChinese!!//args.newsTitle
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.findItem(R.id.language_icon).isVisible = false
//        menu!!.findItem(R.id.action_score).isVisible = false
    }
}