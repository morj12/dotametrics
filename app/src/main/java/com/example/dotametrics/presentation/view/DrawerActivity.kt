package com.example.dotametrics.presentation.view

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.dotametrics.R
import com.example.dotametrics.presentation.view.main.MainActivity
import com.example.dotametrics.presentation.view.patch.PatchActivity
import com.example.dotametrics.presentation.view.teamsearch.TeamSearchActivity
import com.google.android.material.navigation.NavigationView

open class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun setContentView(view: View?) {
        drawerLayout = layoutInflater.inflate(R.layout.activity_drawer, null) as DrawerLayout
        val frameContainer: FrameLayout = drawerLayout.findViewById(R.id.activity_container)
        frameContainer.addView(view)
        super.setContentView(drawerLayout)

        val toolbar: Toolbar = drawerLayout.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: NavigationView = drawerLayout.findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_closed
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_players -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            R.id.nav_heroes -> {}
            R.id.nav_teams -> {
                val intent = Intent(this, TeamSearchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            R.id.nav_patches -> {
                val intent = Intent(this, PatchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
        return false
    }

    protected fun allocateActivityTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }
}