package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.model.teams.matches.TeamMatchesResult
import com.example.dotametrics.databinding.TeamMatchItemBinding
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.GlideRequestOptions.requestOptions

class TeamMatchesAdapter :
    ListAdapter<TeamMatchesResult, TeamMatchesAdapter.ViewHolder>(TeamMatchesCallback()) {

    var onItemClickedListener: ((TeamMatchesResult) -> Unit)? = null

    class TeamMatchesCallback : DiffUtil.ItemCallback<TeamMatchesResult>() {
        override fun areItemsTheSame(oldItem: TeamMatchesResult, newItem: TeamMatchesResult) =
            oldItem.matchId == newItem.matchId

        override fun areContentsTheSame(oldItem: TeamMatchesResult, newItem: TeamMatchesResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: TeamMatchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamMatchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            Glide.with(root)
                .load(item.opposingTeamLogo)
                .apply(requestOptions())
                .into(ivTeamMatchesEnemy)
            item.duration?.let { tvTeamMatchesDuration.text = Datetime.getStringTime(it) }
            tvTeamMatchesDireScore.text = item.direScore.toString()
            tvTeamMatchesRadiantScore.text = item.radiantScore.toString()
            tvTeamMatchesLeague.text = item.leagueName
            tvTeamMatchesEnemyName.text = item.opposingTeamName
            tvTeamMatchesDate.text = item.startTime?.let { Datetime.getDateTime(it) }
            if (item.radiant != null && item.radiantWin != null) {
                val result =
                    if (item.radiant!! && item.radiantWin!! || !item.radiant!! && !item.radiantWin!!)
                        root.context.getString(R.string.win)
                    else root.context.getString(R.string.lose)
                tvTeamMatchesResult.text = result
                if (result == root.context.getString(R.string.win)) {
                    tvTeamMatchesResult.setTextColor(root.context.getColor(R.color.green))
                } else {
                    tvTeamMatchesResult.setTextColor(root.context.getColor(R.color.red))
                }
            }
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }

}