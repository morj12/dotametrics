package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dotametrics.R
import com.example.dotametrics.data.model.constants.patch.PatchResult
import com.example.dotametrics.databinding.PatchSeriesItemBinding
import com.example.dotametrics.util.Datetime

class PatchAdapter : ListAdapter<PatchResult, PatchAdapter.ViewHolder>(PatchSeriesCallback()) {

    var onItemClickedListener: ((PatchResult) -> Unit)? = null

    class PatchSeriesCallback : DiffUtil.ItemCallback<PatchResult>() {
        override fun areItemsTheSame(oldItem: PatchResult, newItem: PatchResult) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PatchResult, newItem: PatchResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: PatchSeriesItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PatchSeriesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvPatchName.text = root.context.getString(R.string.patch_series, item.name)
            val date = Datetime.formatDate(item.date)
            tvPatchDate.text = root.context.getString(R.string.patch_date, date)
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}