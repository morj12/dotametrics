package com.example.dotametrics.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.databinding.PatchnotesHeroItemBinding
import com.example.dotametrics.util.ConstData
import com.example.dotametrics.util.GlideRequestOptions.requestOptions

class PatchNotesHeroesAdapter :
    ListAdapter<Pair<String, List<String>>, PatchNotesHeroesAdapter.ViewHolder>(
        PatchNotesHeroesCallback()
    ) {

    var onItemClickedListener: ((Pair<String, List<String>>) -> Unit)? = null

    class PatchNotesHeroesCallback : DiffUtil.ItemCallback<Pair<String, List<String>>>() {
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

    class ViewHolder(val binding: PatchnotesHeroItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PatchnotesHeroItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val heroInfo = ConstData.heroes.firstOrNull { it.name!!.contains(item.first) }

        with(holder.binding) {
            if (heroInfo != null) {
                tvPatchnotesHeroName.text = heroInfo.localizedName
                Glide.with(root)
                    .load("${URL}${heroInfo.img}")
                    .apply(requestOptions())
                    .into(ivPatchnotesHero)
            } else {
                tvPatchnotesHeroName.text = item.first
            }
            val builder = StringBuilder()
            item.second.forEach { builder.append("<li>$it</li>") }
            tvPatchnotesHeroChanges.text =
                Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_LEGACY)
            root.setOnClickListener {
                onItemClickedListener?.invoke(item)
            }
        }
    }

    companion object {
        private const val URL = "https://api.opendota.com"
    }
}