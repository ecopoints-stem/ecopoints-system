package br.edu.uea.ecopoints.screen

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.ecopoints.databinding.ActivityLoginBinding
import br.edu.uea.ecopoints.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var imageEmployee: ImageView
    private lateinit var imageAdmin: ImageView
    private lateinit var imageDriver: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setupView()
        //setupListeners()
    }

    private fun setupListeners() {
        imageEmployee.setOnClickListener {

        }
        imageAdmin.setOnClickListener {

        }
        imageDriver.setOnClickListener {

        }
    }

    private fun setupView() {
        /*imageEmployee = binding.ivEmployee
        imageAdmin = binding.ivAdmin
        imageDriver = binding.ivDriver*/
    }
}