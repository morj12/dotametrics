package com.example.dotametrics.presentation.view.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityMainBinding
import com.example.dotametrics.presentation.view.herosearch.HeroSearchFragment
import com.example.dotametrics.presentation.view.info.InfoFragment
import com.example.dotametrics.presentation.view.teamsearch.TeamSearchFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerLayout = binding.drawerLayout
        setContentView(binding.root)

        val toolbar: Toolbar = drawerLayout.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_players -> {
                title = resources.getString(R.string.player)
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_mainFragment)
            }

            R.id.nav_heroes -> {
                title = resources.getString(R.string.heroes)
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_heroSearchFragment)
            }

            R.id.nav_teams -> {
                title = resources.getString(R.string.teams)
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_teamSearchFragment)
            }

            R.id.nav_info -> {
                title = resources.getString(R.string.about)
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_infoFragment)
            }
        }
        return false
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_placeholder) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
        currentFragment.let {
            if (it is MainFragment
                || it is HeroSearchFragment
                || it is TeamSearchFragment
                || it is InfoFragment
            ) finish()
            else super.onBackPressed()
        }

    }
}