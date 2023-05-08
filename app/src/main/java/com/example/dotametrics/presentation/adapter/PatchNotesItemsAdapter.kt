package com.example.dotametrics.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.databinding.PatchnotesItemItemBinding
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.util.GlideManager.URL
import com.example.dotametrics.util.GlideManager.requestOptions

class PatchNotesItemsAdapter :
    ListAdapter<Pair<String, List<String>>, PatchNotesItemsAdapter.ViewHolder>(
        PatchNotesItemsCallback()
    ) {

    var onItemClickedListener: ((Pair<String, List<String>>) -> Unit)? = null

    class PatchNotesItemsCallback : DiffUtil.ItemCallback<Pair<String, List<String>>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, List<String>>,
            newItem: Pair<String, List<String>>
        ) =
            oldItem.first == newItem.first

        override fun areContentsTheSame(
            oldItem: Pair<String, List<String>>,
            newItem: Pair<String, List<String>>
        ) =
            oldItem == newItem
    }

    class ViewHolder(val binding: PatchnotesItemItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PatchnotesItemItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val itemInfo = ConstData.items[item.first]

        with(holder.binding) {
            if (itemInfo != null) {
                tvPatchnotesItemName.text = itemInfo.dname
                Glide.with(root)
                    .load("${URL}${itemInfo.img}")
                    .apply(requestOptions(root.context))
                    .into(ivPatchnotesItem)
            } else {
                tvPatchnotesItemName.text = item.first
            }
            val builder = StringBuilder()
            item.second.forEach { builder.append("<li>$it</li>") }
            tvPatchnotesItemChanges.text =
                Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_LEGACY)
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }
}