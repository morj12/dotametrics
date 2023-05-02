package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.model.teams.TeamsResult
import com.example.dotametrics.databinding.TeamsItemBinding
import com.example.dotametrics.util.GlideManager

class TeamSearchAdapter :
    ListAdapter<TeamsResult, TeamSearchAdapter.ViewHolder>(TeamSearchResultCallback()) {

    var onItemClickedListener: ((TeamsResult) -> Unit)? = null

    class TeamSearchResultCallback : DiffUtil.ItemCallback<TeamsResult>() {
        override fun areItemsTheSame(oldItem: TeamsResult, newItem: TeamsResult) =
            oldItem.teamId == newItem.teamId

        override fun areContentsTheSame(oldItem: TeamsResult, newItem: TeamsResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: TeamsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            teamsName.text = item.name
            teamsRating.text = root.context.getString(R.string.team_rating, item.rating.toString())
            Glide.with(this.root)
                .load(item.logoUrl)
                .apply(GlideManager.requestOptions(root.context))
                .into(teamsIcon)
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}