package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.databinding.FavoriteItemBinding
import com.example.dotametrics.util.GlideManager.requestOptions

class FavoriteAdapter :
    ListAdapter<PlayerDbModel, FavoriteAdapter.ViewHolder>(FavoriteCallback()) {

    var onItemClickedListener: ((PlayerDbModel) -> Unit)? = null

    class FavoriteCallback : DiffUtil.ItemCallback<PlayerDbModel>() {
        override fun areItemsTheSame(oldItem: PlayerDbModel, newItem: PlayerDbModel) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: PlayerDbModel, newItem: PlayerDbModel) =
            oldItem == newItem
    }

    class ViewHolder(val binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            favResultName.text = item.name
            favResultId.text = item.id.toString()
            Glide.with(this.root)
                .load(item.avatar)
                .apply(requestOptions(root.context))
                .into(favIcon)

            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }

}