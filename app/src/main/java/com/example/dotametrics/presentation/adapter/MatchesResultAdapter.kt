package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.databinding.MatchItemBinding
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.requestOptions
import com.example.dotametrics.util.LobbyTypeMapper

class MatchesResultAdapter :
    PagedListAdapter<MatchesResult, MatchesResultAdapter.ViewHolder>(MatchesCallback()) {

    var onItemClickedListener: ((MatchesResult) -> Unit)? = null

    class MatchesCallback : DiffUtil.ItemCallback<MatchesResult>() {
        override fun areItemsTheSame(oldItem: MatchesResult, newItem: MatchesResult) =
            oldItem.matchId == newItem.matchId

        override fun areContentsTheSame(oldItem: MatchesResult, newItem: MatchesResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: MatchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MatchItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!

        val heroInfo = ConstData.heroes.firstOrNull { it.id == item.heroId }
        val lobbyInfo = ConstData.lobbies.firstOrNull { it.id == item.lobbyType }

        with(holder.binding) {
            if (heroInfo != null)
                Glide.with(root)
                    .load("${GlideManager.HEROES_URL}/${heroInfo.name?.replace(GlideManager.HEROES_URL_REPLACE, "")}.png")
                    .apply(requestOptions(root.context))
                    .into(ivMatchHero)
            item.startTime?.let { tvMatchDate.text = Datetime.formatDate(it) }
            tvMatchKda.text = "${item.kills} / ${item.deaths} / ${item.assists}"
            setLobbyType(item, lobbyInfo, this)
            setResult(item, this)
            setRank(item, this)

            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }

    private fun setLobbyType(
        item: MatchesResult, lobbyInfo: LobbyTypeResult?, binding: MatchItemBinding
    ) = with(binding) {
        val partyCount = if (item.partySize != null) " x ${item.partySize}" else " x ?"
        tvMatchLobby.text =
            if (lobbyInfo != null)
                root.context.getString(
                    LobbyTypeMapper().getLobbyResource(
                        lobbyInfo.name!!,
                        root.context
                    )
                ) + partyCount
            else item.lobbyType.toString() + partyCount
    }

    private fun setResult(item: MatchesResult, binding: MatchItemBinding) = with(binding) {
        if (isWin(item)) {
            tvMatchWl.text = root.context.getString(R.string.win)
            tvMatchWl.setTextColor(root.resources.getColor(R.color.green))
        } else {
            tvMatchWl.text = root.context.getString(R.string.lose)
            tvMatchWl.setTextColor(root.resources.getColor(R.color.red))
        }

    }

    private fun isWin(item: MatchesResult) =
        if (item.playerSlot != null && item.radiantWin != null)
            ((item.radiantWin!! && item.playerSlot!! < 100)
                    || (item.radiantWin == false && item.playerSlot!! > 100))
        else false


    private fun setRank(item: MatchesResult, binding: MatchItemBinding) = with(binding) {
        val id = when (item.averageRank) {
            null -> R.drawable.r00
            81 -> R.drawable.r80
            else -> root.context.resources.getIdentifier(
                "r${item.averageRank}", "drawable", root.context.packageName
            )
        }
        ivMatchRank.setImageResource(id)
    }

}