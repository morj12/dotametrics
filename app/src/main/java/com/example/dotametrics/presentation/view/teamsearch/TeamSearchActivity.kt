package com.example.dotametrics.presentation.view.teamsearch

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.model.teams.TeamsResult
import com.example.dotametrics.databinding.ActivityTeamSearchBinding
import com.example.dotametrics.presentation.adapter.TeamSearchAdapter
import com.example.dotametrics.presentation.view.DrawerActivity
import com.example.dotametrics.presentation.view.team.TeamActivity
import com.google.android.material.snackbar.Snackbar

class TeamSearchActivity : DrawerActivity() {

    private lateinit var binding: ActivityTeamSearchBinding

    private val viewModel: TeamSearchViewModel by viewModels {
        TeamSearchViewModel.TeamSearchViewModelFactory(applicationContext as App)
    }

    private lateinit var adapter: TeamSearchAdapter

    private val openTeam: (TeamsResult) -> Unit = {
        val intent = Intent(this, TeamActivity::class.java)
        intent.putExtra("team", it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.teams))

        if (viewModel.teams.value == null) viewModel.loadTeams()
        initRecyclerView()
        initListeners()
        observe()
    }

    private fun initRecyclerView() {
        adapter = TeamSearchAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        binding.rcTeams.layoutManager = layoutManager
        binding.rcTeams.adapter = adapter
        adapter.onItemClickedListener = openTeam
    }

    private fun initListeners() = with(binding) {
        btSearchTeams.setOnClickListener {
            binding.btSearchTeams.visibility = View.INVISIBLE
            binding.pbSearchTeams.visibility = View.VISIBLE
            viewModel.filterTeams(
                if (binding.edTeams.text.isNotBlank())
                    edTeams.text.toString()
                else null
            )
        }
    }

    private fun loadData() {
        viewModel.filterTeams()
    }

    private fun observe() {
        viewModel.teams.observe(this) {
            loadData()
        }
        viewModel.filteredTeams.observe(this) {
            adapter.submitList(it) {
                binding.rcTeams.scrollToPosition(0)
                binding.btSearchTeams.visibility = View.VISIBLE
                binding.pbSearchTeams.visibility = View.INVISIBLE
            }
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            binding.btSearchTeams.visibility = View.VISIBLE
            binding.pbSearchTeams.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}