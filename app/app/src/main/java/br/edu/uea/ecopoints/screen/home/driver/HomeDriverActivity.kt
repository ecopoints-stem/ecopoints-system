package br.edu.uea.ecopoints.screen.home.driver

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityHomeDriverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDriverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeDriverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_driver_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationDriver,navController)
    }
}