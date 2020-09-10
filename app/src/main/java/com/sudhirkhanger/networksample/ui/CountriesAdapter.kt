package com.sudhirkhanger.networksample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sudhirkhanger.networksample.databinding.ItemCountryBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class CountriesAdapter(private val expoClickListener: (String?) -> Unit) :
    ListAdapter<Country, CountriesAdapter.ExpoViewHolder>(ExpoDiffUtil()) {

    class ExpoDiffUtil : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpoViewHolder =
        ExpoViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ExpoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpoViewHolder(private val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Country) {
            binding.mainLayout.setOnClickListener { expoClickListener(country.id) }

            Glide.with(binding.backgroundIv.context)
                .load(country.backImage)
                .apply(RequestOptions.bitmapTransform(BlurTransformation()))
                .into(binding.backgroundIv)

            Glide.with(binding.iconIv.context)
                .load(country.iconImage)
                .into(binding.iconIv)

            binding.nameTv.text = country.name
            binding.titleTv.text = country.title
        }
    }
}