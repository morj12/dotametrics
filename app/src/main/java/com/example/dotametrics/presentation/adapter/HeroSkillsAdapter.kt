package com.example.dotametrics.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.remote.model.constants.abilities.AbilityResult
import com.example.dotametrics.databinding.SkillItemBinding
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.URL

class HeroSkillsAdapter :
    ListAdapter<AbilityResult, HeroSkillsAdapter.ViewHolder>(HeroSkillCallback()) {

    class HeroSkillCallback : DiffUtil.ItemCallback<AbilityResult>() {
        override fun areItemsTheSame(oldItem: AbilityResult, newItem: AbilityResult) =
            oldItem.dname == newItem.dname

        override fun areContentsTheSame(oldItem: AbilityResult, newItem: AbilityResult) =
            oldItem == newItem
    }

    class ViewHolder(val binding: SkillItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SkillItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            tvSkillName.text = item.dname
            tvSkillDesc.text = item.desc
            val bkbPierceString = item.bkbPierce.joinToString(separator = ", ")
            tvSkillBkbPierce.text =
                root.context.getString(R.string.hero_bkb_pierce, bkbPierceString)
            val dmgTypeString = item.dmgType.joinToString(separator = ", ")
            tvSkillDmgType.text = root.context.getString(R.string.hero_dmg_type, dmgTypeString)
            val behaviorString = item.behavior.joinToString(separator = ", ")
            tvSkillBhv.text = root.context.getString(R.string.skill_bhv, behaviorString)
            val attrsString = StringBuilder()
            item.attrib.forEach {
                attrsString.append(it.header)
                attrsString.append(" ")
                attrsString.append(it.value.joinToString(separator = ", "))
                attrsString.append("\n")
            }
            tvSkillAttrs.text = attrsString.toString()
            Glide.with(this.root)
                .load("${URL}${item.img}")
                .apply(GlideManager.requestOptions(root.context))
                .into(ivSkillImg)
        }
    }
}