package com.example.secondhiltapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.FragmentDetailsNewsBinding


class NewsDetailsFragment : Fragment(R.layout.fragment_details_news) {

    private val args by navArgs<NewsDetailsFragmentArgs>()

    private var _binding: FragmentDetailsNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsNewsBinding.bind(view)

        binding.apply {
            Glide.with(this@NewsDetailsFragment)
                .load(args.image)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(newsDetailsImage)

            newsTitle.text = args.newsTitle
            newsDescription.text = args.newsText
        }
    }
}