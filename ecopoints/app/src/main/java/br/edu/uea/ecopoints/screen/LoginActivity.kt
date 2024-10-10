package br.edu.uea.ecopoints.screen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityLoginBinding
import br.edu.uea.ecopoints.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListeners()
    }

    private fun setupListeners() {

    }

    private fun setupView() {

    }
}