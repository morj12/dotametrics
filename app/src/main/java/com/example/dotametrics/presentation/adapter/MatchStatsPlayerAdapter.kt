package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dotametrics.domain.entity.remote.matches.Players
import com.example.dotametrics.databinding.MatchStatsItemBinding

class MatchStatsPlayerAdapter :
    ListAdapter<Players, MatchStatsPlayerAdapter.ViewHolder>(MatchStatsPlayerCallback()) {

    class MatchStatsPlayerCallback : DiffUtil.ItemCallback<Players>() {
        override fun areItemsTheSame(oldItem: Players, newItem: Players) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: Players, newItem: Players) =
            oldItem == newItem
    }

    class ViewHolder(val binding: MatchStatsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MatchStatsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvMatchStatsGold.text = item.totalGold.toString()
            tvMatchStatsLasthits.text = item.lastHits.toString()
            tvMatchStatsDenies.text = item.denies.toString()
            item.goldPerMin?.let { tvMatchStatsGpm.text = it.toString() }
            tvMatchStatsXpm.text = item.xpPerMin.toString()
            tvMatchStatsDmg.text = item.heroDamage.toString()
            tvMatchStatsDmgTowers.text = item.towerDamage.toString()
        }
    }

}