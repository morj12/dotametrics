package com.example.dotametrics.presentation.view.match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.databinding.ActivityMatchBinding
import com.google.android.material.snackbar.Snackbar

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    private val viewModel: MatchViewModel by lazy {
        ViewModelProvider(this)[MatchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            binding.tvExample.text = id.toString()
            viewModel.matchId = id.toString()
            viewModel.loadMatch(id.toString())
            observe()
        }
    }

    private fun observe() {
        viewModel.result.observe(this) {
            Log.d("TAG", "observe: ")
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

}
