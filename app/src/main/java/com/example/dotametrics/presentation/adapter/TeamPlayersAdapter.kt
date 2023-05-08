package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dotametrics.data.remote.model.teams.players.TeamPlayersResult
import com.example.dotametrics.databinding.TeamPlayerItemBinding

class TeamPlayersAdapter :
    ListAdapter<TeamPlayersResult, TeamPlayersAdapter.ViewHolder>(TeamPlayersCallback()) {

    var onItemClickedListener: ((TeamPlayersResult) -> Unit)? = null

    class TeamPlayersCallback : DiffUtil.ItemCallback<TeamPlayersResult>() {
        override fun areItemsTheSame(oldItem: TeamPlayersResult, newItem: TeamPlayersResult) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: TeamPlayersResult, newItem: TeamPlayersResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: TeamPlayerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamPlayerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvTeamPlayerItemGames.text = item.gamesPlayed.toString()
            tvTeamPlayerItemName.text = item.name
            tvTeamPlayerItemWins.text = item.wins.toString()
            if (item.gamesPlayed != null && item.wins != null) {
                tvTeamPlayerItemLoses.text = (item.gamesPlayed!! - item.wins!!).toString()
                val winrate = item.wins!!.toDouble() / item.gamesPlayed!! * 100
                tvTeamPlayerItemWinrate.text =
                    "${String.format("%.2f", winrate)}%"
            }
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }

}