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
import com.example.dotametrics.util.GlideManager.ABILITIES_URL

class HeroSkillsAdapter :
    ListAdapter<Pair<String, AbilityResult>, HeroSkillsAdapter.ViewHolder>(HeroSkillCallback()) {

    class HeroSkillCallback : DiffUtil.ItemCallback<Pair<String, AbilityResult>>() {
        override fun areItemsTheSame(oldItem: Pair<String, AbilityResult>, newItem: Pair<String, AbilityResult>) =
            oldItem.first == newItem.first

        override fun areContentsTheSame(oldItem: Pair<String, AbilityResult>, newItem: Pair<String, AbilityResult>) =
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
            tvSkillName.text = item.second.dname
            tvSkillDesc.text = item.second.desc
            val bkbPierceString = item.second.bkbPierce.joinToString(separator = ", ")
            tvSkillBkbPierce.text =
                root.context.getString(R.string.hero_bkb_pierce, bkbPierceString)
            val dmgTypeString = item.second.dmgType.joinToString(separator = ", ")
            tvSkillDmgType.text = root.context.getString(R.string.hero_dmg_type, dmgTypeString)
            val behaviorString = item.second.behavior.joinToString(separator = ", ")
            tvSkillBhv.text = root.context.getString(R.string.skill_bhv, behaviorString)
            val cdString = item.second.cd.joinToString(separator = ", ")
            tvSkillCd.text = root.context.getString(R.string.skill_cd, cdString)
            val mcString = item.second.mc.joinToString(separator = ", ")
            tvSkillMc.text = root.context.getString(R.string.skill_mc, mcString)
            val attrsString = StringBuilder()
            item.second.attrib.forEach {
                attrsString.append(it.header)
                attrsString.append(" ")
                attrsString.append(it.value.joinToString(separator = ", "))
                attrsString.append("\n")
            }
            tvSkillAttrs.text = attrsString.toString()
            Glide.with(this.root)
                .load("${ABILITIES_URL}/${item.first}.png")
                .apply(GlideManager.requestOptions(root.context))
                .into(ivSkillImg)
        }
    }
}