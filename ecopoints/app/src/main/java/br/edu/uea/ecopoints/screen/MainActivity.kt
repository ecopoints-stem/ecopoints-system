package br.edu.uea.ecopoints.screen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.ecopoints.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageEmployee: ImageView
    private lateinit var imageAdmin: ImageView
    private lateinit var imageDriver: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListeners()
    }

    private fun setupListeners() {
        imageEmployee.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("roleSelected","employee")
            startActivity(intent)
        }
        imageAdmin.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("roleSelected","admin")
            startActivity(intent)
        }
        imageDriver.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("roleSelected","driver")
            startActivity(intent)
        }
    }

    private fun setupView() {
        imageEmployee = binding.ivEmployee
        imageAdmin = binding.ivAdmin
        imageDriver = binding.ivDriver
    }
}