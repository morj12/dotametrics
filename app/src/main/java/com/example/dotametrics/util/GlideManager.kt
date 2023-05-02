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

    const val URL = "https://api.opendota.com"

}