package com.example.dotametrics.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.presentation.adapter.SearchResultAdapter
import com.example.dotametrics.data.model.search.SearchResult
import com.example.dotametrics.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchResultAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val openAccount: (SearchResult) -> Unit = {
        // TODO: open main profile info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        initRecyclerView()
        observe()
        initListeners()
    }

    private fun initRecyclerView() {
        adapter = SearchResultAdapter()
        binding.rcMain.layoutManager = LinearLayoutManager(this)
        binding.rcMain.adapter = adapter
        adapter.onItemClickedListener = openAccount
    }

    private fun observe() {
        viewModel.results.observe(this) {
            adapter.submitList(it) {
                binding.rcMain.scrollToPosition(0)
            }
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initListeners() {
        binding.edMainUsername.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            true
        }
//        binding.btMainSearch.setOnClickListener { search() }
        binding.btSearch.setOnClickListener { search() }
    }


    private fun search() {
        val user = binding.edMainUsername.text.toString()
        viewModel.search(user)
    }

}