package com.example.dotametrics.presentation.view.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityMainBinding
import com.example.dotametrics.presentation.view.herosearch.HeroSearchFragment
import com.example.dotametrics.presentation.view.info.InfoFragment
import com.example.dotametrics.presentation.view.teamsearch.TeamSearchFragment
import com.example.dotametrics.util.DotaLogger
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerLayout = binding.drawerLayout
        setContentView(binding.root)

        val toolbar: Toolbar = drawerLayout.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_placeholder) as NavHostFragment
        navController = navHostFragment.navController

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_closed
        )

        val navView: NavigationView = drawerLayout.findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        title = resources.getString(R.string.player)

        onBackPressedDispatcher.addCallback(this) {
            handleBackPressed()
        }

        observeUiEvents()
    }

    private fun observeUiEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                DotaLogger.uiEvents.collectLatest { event ->
                    if (event == "RATE_LIMIT_EXCEEDED") {
                        Snackbar.make(
                            binding.root,
                            R.string.rate_limit_error,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun handleBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_placeholder) as NavHostFragment
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
            
            if (currentFragment is MainFragment
                || currentFragment is HeroSearchFragment
                || currentFragment is TeamSearchFragment
                || currentFragment is InfoFragment
            ) {
                finish()
            } else {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_players -> {
                title = resources.getString(R.string.player)
                navController.navigate(R.id.action_mainFragment)
            }

            R.id.nav_heroes -> {
                title = resources.getString(R.string.heroes)
                navController.navigate(R.id.action_heroSearchFragment)
            }

            R.id.nav_teams -> {
                title = resources.getString(R.string.teams)
                navController.navigate(R.id.action_teamSearchFragment)
            }

            R.id.nav_info -> {
                title = resources.getString(R.string.about)
                navController.navigate(R.id.action_infoFragment)
            }
        }
        return true
    }
}
