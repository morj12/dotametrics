package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.domain.entity.remote.teams.heroes.TeamHeroesResult
import com.example.dotametrics.databinding.TeamHeroItemBinding
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.HEROES_URL
import com.example.dotametrics.util.GlideManager.requestOptions

class TeamHeroesAdapter :
    ListAdapter<TeamHeroesResult, TeamHeroesAdapter.ViewHolder>(TeamHeroesCallback()) {

    class TeamHeroesCallback : DiffUtil.ItemCallback<TeamHeroesResult>() {
        override fun areItemsTheSame(oldItem: TeamHeroesResult, newItem: TeamHeroesResult) =
            oldItem.heroId == newItem.heroId

        override fun areContentsTheSame(oldItem: TeamHeroesResult, newItem: TeamHeroesResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: TeamHeroItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamHeroItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val heroInfo = ConstData.heroes.firstOrNull { it.id == item.heroId }
        with(holder.binding) {
            if (heroInfo != null) {
                Glide.with(root)
                    .load("${HEROES_URL}/${heroInfo.name?.replace(GlideManager.HEROES_URL_REPLACE, "")}.png")
                    .apply(requestOptions(root.context))
                    .into(ivTeamHeroItemName)
            }
            tvTeamHeroItemGames.text = item.gamesPlayed.toString()
            tvTeamHeroItemWins.text = item.wins.toString()
            if (item.gamesPlayed != null && item.wins != null) {
                tvTeamHeroItemLoses.text = (item.gamesPlayed!! - item.wins!!).toString()
                val winrate = item.wins!!.toDouble() / item.gamesPlayed!! * 100
                tvTeamHeroItemWinrate.text =
                    "${String.format("%.2f", winrate)}%"
            }

        }
    }

}