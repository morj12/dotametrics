package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dotametrics.data.remote.model.players.totals.TotalsResult
import com.example.dotametrics.databinding.TotalsItemBinding
import com.example.dotametrics.util.StatsMapper

class TotalsAdapter : ListAdapter<TotalsResult, TotalsAdapter.ViewHolder>(TotalsCallback()) {

    class TotalsCallback : DiffUtil.ItemCallback<TotalsResult>() {
        override fun areItemsTheSame(oldItem: TotalsResult, newItem: TotalsResult) =
            oldItem.field == newItem.field

        override fun areContentsTheSame(oldItem: TotalsResult, newItem: TotalsResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: TotalsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TotalsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            if (item.field != null && item.sum != null && item.n != null) {
                tvTotalsName.text =
                    root.context.getString(StatsMapper().getStatsResource(item.field!!, root.context))
                tvTotalsValue.text = String.format("%.2f", item.sum!! / item.n!!)
            }
        }
    }
}