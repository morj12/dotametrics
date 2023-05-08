package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.R
import com.example.dotametrics.data.remote.model.search.SearchResult
import com.example.dotametrics.databinding.SearchResultItemBinding
import com.example.dotametrics.util.GlideManager.requestOptions

class SearchResultAdapter :
    ListAdapter<SearchResult, SearchResultAdapter.ViewHolder>(SearchResultCallback()) {

    var onItemClickedListener: ((SearchResult) -> Unit)? = null

    class SearchResultCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: SearchResultItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            searchResultName.text = item.personaname
            searchResultId.text = item.accountId.toString()
            val date = Datetime.formatDate(item.lastMatchTime)
            searchResultLastMatch.text = root.context.getString(R.string.last_match_time, date)
            Glide.with(this.root)
                .load(item.avatarfull)
                .apply(requestOptions(root.context))
                .into(searchIcon)
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}