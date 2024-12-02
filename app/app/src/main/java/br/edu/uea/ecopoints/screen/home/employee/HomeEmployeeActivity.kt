package br.edu.uea.ecopoints.screen.home.employee

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityHomeEmployeeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeEmployeeActivity : AppCompatActivity() {
    private val binding: ActivityHomeEmployeeBinding by lazy { ActivityHomeEmployeeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_employee_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationEmployee,navController)
    }
}