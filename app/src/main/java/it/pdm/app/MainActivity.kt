package it.pdm.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import it.pdm.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        setupWithNavController(binding.bottomNavigationView, navController)

        //quando non siamo nei fragment principali la navbar viene nascosta
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id != R.id.petFragment && destination.id != R.id.homeFragment && destination.id != R.id.settingsFragment) {

                binding.bottomNavigationView.visibility = View.INVISIBLE //CON IL .GONE il fragment si bugga e sarà biaco
            } else {                                                     //CON .INVISIBLE E .VISIBLE IL FRAGMENT FUNZIONA CORRETTAMENTE MA CI SONO DEI BUG VISIVI

                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        val navController = findNavController(R.id.activity_main_nav_host_fragment)
        val config = AppBarConfiguration(navController.graph)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id != R.id.petFragment && destination.id != R.id.homeFragment && destination.id != R.id.settingsFragment) {
                    supportActionBar?.show() // to show
                    findViewById<Toolbar>(R.id.toolbarTb).setupWithNavController(navController, config)//per tornare indietro nei fragments
                }else{
                supportActionBar?.hide() // to hide
            }
        }
    }
}

