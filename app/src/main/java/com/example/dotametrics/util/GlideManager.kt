package com.example.dotametrics.util

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dotametrics.R

object GlideManager {

    fun requestOptions(context: Context): RequestOptions {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 24f
        circularProgressDrawable.setColorSchemeColors(context.getColor(R.color.purple_500))
        circularProgressDrawable.start()

        return RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_placeholder)
    }

    const val ITEMS_URL = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/items"
    const val HEROES_URL = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes"
    const val ABILITIES_URL = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/abilities"
    const val HEROES_URL_REPLACE = "npc_dota_hero_"

}