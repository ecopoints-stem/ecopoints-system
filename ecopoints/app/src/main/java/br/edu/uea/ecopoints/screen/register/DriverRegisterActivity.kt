package br.edu.uea.ecopoints.screen.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityDriverRegisterBinding

class DriverRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}