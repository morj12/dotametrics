package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.remote.model.players.peers.PeersResult
import com.example.dotametrics.databinding.PeersItemBinding
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.GlideManager.requestOptions

class PeersAdapter : ListAdapter<PeersResult, PeersAdapter.ViewHolder>(PeersCallback()) {

    var onItemClickedListener: ((PeersResult) -> Unit)? = null

    class PeersCallback : DiffUtil.ItemCallback<PeersResult>() {
        override fun areItemsTheSame(oldItem: PeersResult, newItem: PeersResult) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: PeersResult, newItem: PeersResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: PeersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PeersItemBinding.inflate(
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
                .load(item.avatarfull)
                .apply(requestOptions(root.context))
                .into(ivPeerImg)
            tvPeerName.text = item.personaname
            tvPeerCount.text = item.withGames.toString()
            if (item.withWin != null && item.withGames != null) {
                val winrate = item.withWin!!.toDouble() / item.withGames!! * 100
                tvPeerWinrate.text =
                    "${String.format("%.2f", winrate)}%"
                tvPeerWinrate.setTextColor(
                    when {
                        winrate > 55 -> root.context.getColor(R.color.green)
                        winrate < 45 -> root.context.getColor(R.color.red)
                        else -> root.context.getColor(R.color.gray)
                    }
                )
            }
            item.lastPlayed?.let {
                val date = Datetime.formatDate(it)
                tvPeerLastDate.text = root.context.getString(R.string.last_match_time, date)
            }

            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}