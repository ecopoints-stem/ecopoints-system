package br.edu.uea.ecopoints.screen.home.admin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityHomeAdminBinding
import br.edu.uea.ecopoints.screen.state.home.HomeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAdminActivity : AppCompatActivity() {
    private val binding: ActivityHomeAdminBinding by lazy { ActivityHomeAdminBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_admin_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationAdmin,navController)
        viewModel.state.observe(this){ state : HomeState ->
            binding.tvMessageResult.isVisible = state.isErrorMessageVisible
            binding.pbLoading.isVisible = state.isProgressVisible
            binding.tvMessageResult.text = state.errorMessage
            state.errorResponseApi?.let { error ->
                val detailsMessage = error.details.entries.joinToString(separator = "\n") {
                    "${it.key}: ${it.value ?: "Informação não disponível"}"
                }
                AlertDialog.Builder(this).setTitle(
                    "ERRO STATUS ${error.status}"
                ).setMessage(
                    detailsMessage
                ).setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }.show()
            }
        }
    }
}