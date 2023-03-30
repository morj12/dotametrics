package com.example.dotametrics.presentation.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.presentation.adapter.SearchResultAdapter
import com.example.dotametrics.data.model.search.SearchResult
import com.example.dotametrics.databinding.ActivityMainBinding
import com.example.dotametrics.presentation.view.account.AccountActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchResultAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val openAccount: (SearchResult) -> Unit = {
        val intent = Intent(this, AccountActivity::class.java)
        intent.putExtra("id", it.accountId)
        startActivity(intent)
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
                binding.btSearch.visibility = View.VISIBLE
                binding.pbSearch.visibility = View.INVISIBLE
            }
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            binding.btSearch.visibility = View.VISIBLE
            binding.pbSearch.visibility = View.INVISIBLE
        }
    }

    private fun initListeners() {
        binding.edMainUsername.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            true
        }
        binding.btSearch.setOnClickListener { search() }
    }


    private fun search() {
        val user = binding.edMainUsername.text.toString()
        viewModel.search(user)
        binding.btSearch.visibility = View.INVISIBLE
        binding.pbSearch.visibility = View.VISIBLE
    }

}