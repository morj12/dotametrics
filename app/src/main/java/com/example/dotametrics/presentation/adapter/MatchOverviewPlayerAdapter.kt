package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.items.ItemResult
import com.example.dotametrics.domain.entity.remote.matches.Players
import com.example.dotametrics.databinding.MatchOverviewItemBinding
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.HEROES_URL
import com.example.dotametrics.util.GlideManager.ITEMS_URL
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
                    .load("${HEROES_URL}/${heroInfo.name?.replace(GlideManager.HEROES_URL_REPLACE, "")}.png")
                    .apply(requestOptions(root.context))
                    .into(ivMatchOverviewHero)
            }
            setRank(item, this)
            tvMatchOverviewLvl.text = item.level.toString()
            tvMatchOverviewName.text =
                item.personaname ?: root.context.getString(R.string.anonymous)
            tvMatchOverviewKda.text = "${item.kills} / ${item.deaths} / ${item.assists}"
            setItems(item, this)
            setPartyColor(item, this)

            if (item.personaname != null) {
                root.setOnClickListener {
                    onItemClickedListener?.invoke(item)
                }
            }
        }
    }

    private fun setPartyColor(item: Players, binding: MatchOverviewItemBinding) {
        if (item.partyId != null) {
            binding.tvMatchOverviewParty.text = item.partyId.toString()
            when (item.partyId) {
                0 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_0))
                1 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_1))
                2 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_2))
                3 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_3))
                4 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_4))
                5 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_5))
                6 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_6))
                7 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_7))
                8 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_8))
                9 -> binding.tvMatchOverviewParty.setTextColor(binding.root.context.getColor(R.color.p_9))
            }
        }
    }

    private fun setItems(player: Players, binding: MatchOverviewItemBinding) {
        var item: Pair<String, ItemResult>
        if (player.item0 != null && player.item0 != 0) {
            item = ConstData.items.entries.first { it.value.id == player.item0 }.toPair()

            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem0)
        }
        if (player.item1 != null && player.item1 != 0) {
            item = ConstData.items.entries.first { it.value.id == player.item1 }.toPair()
            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem1)
        }
        if (player.item2 != null && player.item2 != 0) {
            item = ConstData.items.entries.first { it.value.id == player.item2 }.toPair()
            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem2)
        }
        if (player.item3 != null && player.item3 != 0) {
            item = ConstData.items.entries.first { it.value.id == player.item3 }.toPair()
            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem3)
        }
        if (player.item4 != null && player.item4 != 0) {
            item = ConstData.items.entries.first { it.value.id == player.item4 }.toPair()
            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem4)
        }
        if (player.item5 != null && player.item5 != 0) {
            item = ConstData.items.entries.first { it.value.id == player.item5 }.toPair()
            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
                .apply(requestOptions(binding.root.context))
                .into(binding.tvMatchOverviewItem5)
        }
        if (player.itemNeutral != null && player.itemNeutral != 0) {
            item = ConstData.items.entries.first { it.value.id == player.itemNeutral }.toPair()
            Glide.with(binding.root)
                .load("${ITEMS_URL}/${item.first}.png")
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