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
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityMainBinding
import com.example.dotametrics.presentation.view.herosearch.HeroSearchFragment
import com.example.dotametrics.presentation.view.info.InfoFragment
import com.example.dotametrics.presentation.view.patch.PatchFragment
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

        setTitle(resources.getString(R.string.player))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_players -> {
                setTitle(resources.getString(R.string.player))
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_mainFragment)
            }

            R.id.nav_heroes -> {
                setTitle(resources.getString(R.string.heroes))
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_heroSearchFragment)
            }

            R.id.nav_teams -> {
                setTitle(resources.getString(R.string.teams))
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_teamSearchFragment)
            }

            R.id.nav_patches -> {
                setTitle(resources.getString(R.string.patches))
                findNavController(R.id.fragment_placeholder).navigate(R.id.action_patchFragment)
            }

            R.id.nav_info -> {
                setTitle(resources.getString(R.string.about))
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
                || it is PatchFragment
                || it is InfoFragment
            ) finish()
            else super.onBackPressed()
        }

    }
}