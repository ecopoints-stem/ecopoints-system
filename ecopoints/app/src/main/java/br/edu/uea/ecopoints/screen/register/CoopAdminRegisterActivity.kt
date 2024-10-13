package br.edu.uea.ecopoints.screen.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.ecopoints.databinding.ActivityCoopAdminRegisterBinding

class CoopAdminRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoopAdminRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoopAdminRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}