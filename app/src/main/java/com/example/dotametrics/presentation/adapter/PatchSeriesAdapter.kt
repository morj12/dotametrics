package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.patch.PatchNotesResult
import com.example.dotametrics.databinding.PatchItemBinding

class PatchSeriesAdapter :
    ListAdapter<Pair<String, PatchNotesResult>, PatchSeriesAdapter.ViewHolder>(PatchCallback()) {

    var onItemClickedListener: ((Pair<String, PatchNotesResult>) -> Unit)? = null

    class PatchCallback : DiffUtil.ItemCallback<Pair<String, PatchNotesResult>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, PatchNotesResult>,
            newItem: Pair<String, PatchNotesResult>
        ) =
            oldItem.first == newItem.first

        override fun areContentsTheSame(
            oldItem: Pair<String, PatchNotesResult>,
            newItem: Pair<String, PatchNotesResult>
        ) =
            oldItem == newItem
    }

    class ViewHolder(val binding: PatchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PatchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvPatchId.text = item.first
            tvPatchGeneralChanges.text = root.context.getString(
                R.string.patch_general_count,
                item.second.general.size.toString()
            )
            tvPatchHeroChanges.text = root.context.getString(
                R.string.patch_hero_count,
                item.second.heroes.size.toString()
            )
            tvPatchItemChanges.text = root.context.getString(
                R.string.patch_item_count,
                item.second.items.size.toString()
            )
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}