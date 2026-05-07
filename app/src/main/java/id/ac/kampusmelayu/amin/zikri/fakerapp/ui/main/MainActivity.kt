package id.ac.kampusmelayu.amin.zikri.fakerapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import id.ac.kampusmelayu.amin.zikri.fakerapp.R
import id.ac.kampusmelayu.amin.zikri.fakerapp.databinding.ActivityMainBinding
import id.ac.kampusmelayu.amin.zikri.fakerapp.ui.about.AboutFragment
import id.ac.kampusmelayu.amin.zikri.fakerapp.ui.persons.PersonsFragment
import id.ac.kampusmelayu.amin.zikri.fakerapp.ui.profile.ProfileDeveloperFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        // Setup drawer toggle (hamburger icon yang bisa dianimasikan)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Listener menu drawer
        binding.navView.setNavigationItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_persons -> PersonsFragment()

                R.id.nav_profile_developer -> ProfileDeveloperFragment()
                R.id.nav_about -> AboutFragment()
                else -> PersonsFragment()
            }
            replaceFragment(fragment, item.title?.toString() ?: getString(R.string.app_name))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Pilih default menu Persons saat pertama dibuka
        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_persons)
            replaceFragment(PersonsFragment(), getString(R.string.menu_persons))
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}
