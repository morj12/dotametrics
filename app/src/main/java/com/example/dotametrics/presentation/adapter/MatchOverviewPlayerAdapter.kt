package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.model.constants.items.ItemResult
import com.example.dotametrics.data.model.matches.Players
import com.example.dotametrics.databinding.MatchOverviewItemBinding
import com.example.dotametrics.util.ConstData
import com.example.dotametrics.util.GlideManager.URL
import com.example.dotametrics.util.GlideManager.requestOptions

class MatchOverviewPlayerAdapter :
    ListAdapter<Players, MatchOverviewPlayerAdapter.ViewHolder>(MatchOverviewPlayerCallback()) {

    var onItemClickedListener: ((Players) -> Unit)? = null

    class MatchOverviewPlayerCallback : DiffUtil.ItemCallback<Players>() {
        override fun areItemsTheSame(oldItem: Players, newItem: Players) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: Players, newItem: Players) =
            oldItem == newItem
    }

    class ViewHolder(val binding: MatchOverviewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MatchOverviewItemBinding.inflate(
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
                    .load("${URL}${heroInfo.img}")
                    .apply(requestOptions(root.context))
                    .into(ivMatchOverviewHero)
            }
            setRank(item, this)
            tvMatchOverviewLvl.text = item.level.toString()
            tvMatchOverviewName.text =
                item.personaname ?: root.context.getString(R.string.anonymous)
            tvMatchOverviewKda.text = "${item.kills} / ${item.deaths} / ${item.assists}"
            setItems(item, this)

            if (item.personaname != null) {
                root.setOnClickListener {
                    onItemClickedListener?.invoke(item)
                }
            }
        }
    }

    private fun setItems(player: Players, binding: MatchOverviewItemBinding) {
        var item: ItemResult
        if (player.item0 != null && player.item0 != 0) {
            item = ConstData.items.values.first { it.id == player.item0 }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem0)
        }
        if (player.item1 != null && player.item1 != 0) {
            item = ConstData.items.values.first { it.id == player.item1 }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem1)
        }
        if (player.item2 != null && player.item2 != 0) {
            item = ConstData.items.values.first { it.id == player.item2 }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem2)
        }
        if (player.item3 != null && player.item3 != 0) {
            item = ConstData.items.values.first { it.id == player.item3 }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem3)
        }
        if (player.item4 != null && player.item4 != 0) {
            item = ConstData.items.values.first { it.id == player.item4 }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem4)
        }
        if (player.item5 != null && player.item5 != 0) {
            item = ConstData.items.values.first { it.id == player.item5 }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem5)
        }
        if (player.itemNeutral != null && player.itemNeutral != 0) {
            item = ConstData.items.values.first { it.id == player.itemNeutral }
            Glide.with(binding.root)
                .load("${URL}${item.img}")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItemn)
        }
    }

    private fun setRank(item: Players, binding: MatchOverviewItemBinding) = with(binding) {
        val id = when (item.rankTier) {
            null -> R.drawable.r00
            else -> root.context.resources.getIdentifier(
                "r${item.rankTier}", "drawable", root.context.packageName
            )
        }
        ivMatchOverviewRank.setImageResource(id)
    }
}