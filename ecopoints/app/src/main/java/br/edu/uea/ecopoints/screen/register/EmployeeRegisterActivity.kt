package br.edu.uea.ecopoints.screen.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityEmployeeRegisterBinding

class EmployeeRegisterActivity : AppCompatActivity() {
    private val binding : ActivityEmployeeRegisterBinding by lazy { ActivityEmployeeRegisterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}