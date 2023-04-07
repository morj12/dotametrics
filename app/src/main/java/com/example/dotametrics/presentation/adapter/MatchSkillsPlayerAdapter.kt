package com.example.dotametrics.presentation.adapter

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.model.matches.Players
import com.example.dotametrics.databinding.MatchSkillsItemBinding
import com.example.dotametrics.util.ConstData
import com.example.dotametrics.util.GlideRequestOptions

class MatchSkillsPlayerAdapter(private val activity: AppCompatActivity) :
    ListAdapter<Players, MatchSkillsPlayerAdapter.ViewHolder>(MatchSkillsPlayerCallback()) {

    private var skillCount = 0

    class MatchSkillsPlayerCallback : DiffUtil.ItemCallback<Players>() {
        override fun areItemsTheSame(oldItem: Players, newItem: Players) =
            oldItem.accountId == newItem.accountId

        override fun areContentsTheSame(oldItem: Players, newItem: Players) =
            oldItem == newItem
    }

    class ViewHolder(val binding: MatchSkillsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MatchSkillsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            // Get cell size
            val width = activity.windowManager.defaultDisplay.width
            val dpWidth = (width / activity.resources.displayMetrics.density)
            val cellSize = (dpWidth / ITEMS_PER_ROW)


            for (i in 0..1) {
                val tableRow = TableRow(root.context)
                for (j in 0 until ITEMS_PER_ROW) {
                    if (skillCount < item.abilityUpgradesArr.size) {
                        val abilityId = item.abilityUpgradesArr[skillCount]
                        val abilityName = ConstData.abilityIds[abilityId.toString()]!!
                        val ability = ConstData.abilities[abilityName]!!
                        val type =
                            when {
                                abilityName.contains("attributes") -> "attributes"
                                abilityName.contains("special_bonus") -> "talent"
                                else -> "ability"
                            }

                        val cell =
                            if (type == "talent" || type == "attributes") TextView(root.context)
                            else ImageView(root.context)

                        val typedSize = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            cellSize,
                            activity.resources.displayMetrics
                        ).toInt()
                        cell.layoutParams = TableRow.LayoutParams(typedSize, typedSize)

                        if (cell is TextView) {
                            if (type == "talent") {
                                cell.text = ability.dname
                                cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6f)
                                cell.setBackgroundResource(R.drawable.talent)
                                cell.textAlignment = TEXT_ALIGNMENT_CENTER
                            } else {
                                cell.setBackgroundResource(R.drawable.attr)
                            }
                        } else if (cell is ImageView) {
                            Glide.with(root)
                                .load("${URL}${ability.img}")
                                .apply(GlideRequestOptions.requestOptions())
                                .into(cell)
                        }
                        skillCount++
                        tableRow.addView(cell)
                    }

                }
                tlSkills.addView(tableRow)
            }
        }
        skillCount = 0

    }

    companion object {
        private const val URL = "https://api.opendota.com"
        private const val ITEMS_PER_ROW = 15
    }
}