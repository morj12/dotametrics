package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.players.heroes.PlayerHeroResult
import com.example.dotametrics.databinding.AccHeroItemBinding
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.HEROES_URL
import com.example.dotametrics.util.GlideManager.requestOptions

class PlayerHeroesAdapter :
    ListAdapter<PlayerHeroResult, PlayerHeroesAdapter.ViewHolder>(HeroesCallback()) {

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

        val heroInfo = ConstData.heroes.firstOrNull { it.id == item.heroId?.toInt() }

        with(holder.binding) {
            if (heroInfo != null && item.games != 0) {
                Glide.with(root)
                    .load("${HEROES_URL}/${heroInfo.name?.replace(GlideManager.HEROES_URL_REPLACE, "")}.png")
                    .apply(requestOptions(root.context))
                    .into(ivAccHeroImg)
                tvAccHeroName.text = heroInfo.localizedName
                tvAccHeroCount.text = item.games.toString()
                if (item.win != null && item.games != null) {
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
                item.lastPlayed?.let {
                    val date = Datetime.formatDate(it)
                    tvHeroLastDate.text = root.context.getString(R.string.last_match_time, date)
                }

            }
        }
    }
}