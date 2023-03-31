package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dotametrics.data.model.players.matches.MatchesResult
import com.example.dotametrics.databinding.MatchItemBinding

class MatchesResultAdapter: PagedListAdapter<MatchesResult, MatchesResultAdapter.ViewHolder>(MatchesCallback()) {

    class MatchesCallback : DiffUtil.ItemCallback<MatchesResult>() {
        override fun areItemsTheSame(oldItem: MatchesResult, newItem: MatchesResult) =
            oldItem.matchId == newItem.matchId

        override fun areContentsTheSame(oldItem: MatchesResult, newItem: MatchesResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: MatchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MatchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvMatchId.text = item!!.matchId.toString()
        }
    }


}