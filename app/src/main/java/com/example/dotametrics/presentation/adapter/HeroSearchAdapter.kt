package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.dotametrics.data.remote.model.constants.heroes.HeroResult
import com.example.dotametrics.databinding.SearchHeroItemBinding
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.URL

class HeroSearchAdapter :
    ListAdapter<HeroResult, HeroSearchAdapter.ViewHolder>(HeroSearchCallback()) {

    var onItemClickedListener: ((HeroResult) -> Unit)? = null

    class HeroSearchCallback : DiffUtil.ItemCallback<HeroResult>() {
        override fun areItemsTheSame(oldItem: HeroResult, newItem: HeroResult) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: HeroResult, newItem: HeroResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: SearchHeroItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchHeroItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvHeroName.text = item.localizedName
            Glide.with(this.root)
                .load("${URL}${item.img}")
                .apply(GlideManager.requestOptions(root.context))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivHeroImg)
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}