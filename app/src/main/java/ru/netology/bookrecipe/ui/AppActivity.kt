package ru.netology.bookrecipe.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import ru.netology.bookrecipe.R
import ru.netology.bookrecipe.databinding.AppActivityBinding

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = AppActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bNav = binding.bNav

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment_container) as NavHostFragment

        val navController = navHostFragment.navController
        setupWithNavController(bNav, navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.filterFragmentSwitch,
                R.id.favoriteFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.recipeCreateFragment2
                || destination.id == R.id.filterSetupFragment
                || destination.id == R.id.stageCreateFragment
                || destination.id == R.id.recipeViewFragment
            ) {
                bNav.visibility = View.GONE
            } else {
                bNav.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

}