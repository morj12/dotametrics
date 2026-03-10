package com.example.dotametrics.util

import android.view.View
import com.example.dotametrics.R
import com.google.android.material.snackbar.Snackbar

fun showError(error: String, rootView: View) {
    if (error == "RATE_LIMIT_EXCEEDED") {
        Snackbar.make(rootView, R.string.rate_limit_error, Snackbar.LENGTH_LONG).show()
    }
}
