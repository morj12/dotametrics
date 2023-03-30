package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.databinding.AccHeroItemBinding

class PlayerHeroesAdapter :
    ListAdapter<PlayerHeroResult, PlayerHeroesAdapter.ViewHolder>(HeroesCallback()) {

    var heroes = listOf<HeroResult>()

    class HeroesCallback : DiffUtil.ItemCallback<PlayerHeroResult>() {
        override fun areItemsTheSame(oldItem: PlayerHeroResult, newItem: PlayerHeroResult) =
            oldItem.heroId == newItem.heroId

        override fun areContentsTheSame(oldItem: PlayerHeroResult, newItem: PlayerHeroResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: AccHeroItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AccHeroItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val heroInfo = heroes.firstOrNull { it.id == item.heroId?.toInt() }
        with(holder.binding) {
            if (heroInfo != null && item.games != 0) {
                Glide.with(root)
                    .load("$URL${heroInfo.img}")
                    .placeholder(R.drawable.ic_person)
                    .into(ivAccHeroImg)
                tvAccHeroName.text = heroInfo?.localizedName ?: item.heroId
                tvAccHeroCount.text = item.games.toString()
                val winrate = item.win!!.toDouble() / item.games!! * 100
                tvAccHeroWinrate.text =
                    "${String.format("%.2f", winrate)}%"
                tvAccHeroWinrate.setTextColor(
                    when {
                        winrate > 55 -> root.context.getColor(R.color.green)
                        winrate < 45 -> root.context.getColor(R.color.red)
                        else -> root.context.getColor(R.color.gray)
                    }
                )
            }
        }
    }

    companion object {
        private const val URL = "https://api.opendota.com"
    }
}