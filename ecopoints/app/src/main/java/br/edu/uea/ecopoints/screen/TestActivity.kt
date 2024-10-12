package br.edu.uea.ecopoints.screen

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.databinding.ActivityTestBinding
import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.domain.entity.Employee
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    @Inject lateinit var ecoApi: EcoApi
    @Inject lateinit var shared: SharedPreferences
    private lateinit var binding: ActivityTestBinding
    private lateinit var button: MaterialButton
    private lateinit var tvUserInfo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListeners()
    }
    private fun setupView(){
        button = binding.btGetUserInfo
        tvUserInfo = binding.tvGetUserInfo
    }
    private fun setupListeners(){
        button.setOnClickListener {
            // Verifica o perfil do atual usuário
            val id : Long = shared.getLong("id",0L)
            val role: String = shared.getString("role","") ?: ""
            Log.i("ECO","Role $role Id $id")
            if(id!=0L && role.isNotBlank()){
                lifecycleScope.launch {
                    if(role=="ADMINISTRATOR"){
                        val adminResponse : Response<CoopAdmin> = withContext(Dispatchers.IO){ecoApi.findAdminById(id)}
                        if(adminResponse.isSuccessful){
                            Log.i("ECO","Admin Response ${adminResponse.body().toString()}")
                            with(Dispatchers.Main) {
                                tvUserInfo.text = adminResponse.body().toString()
                            }
                        } else{
                            Log.i("ECO","Admin Response ${adminResponse.code()}")
                        }
                    } else if(role=="DRIVER"){
                        val driverResponse : Response<Driver> = withContext(Dispatchers.IO){ecoApi.findDriverById(id)}
                        if(driverResponse.isSuccessful){
                            Log.i("ECO","Driver Response ${driverResponse.body().toString()}")
                            with(Dispatchers.Main) {
                                tvUserInfo.text = driverResponse.body().toString()
                            }
                        } else{
                            Log.i("ECO","Driver Response ${driverResponse.code()}")
                        }
                    } else if(role=="EMPLOYEE"){
                        val employeeResponse : Response<Employee> = withContext(Dispatchers.IO){ecoApi.findEmployeeById(id)}
                        if(employeeResponse.isSuccessful){
                            Log.i("ECO","Employee Response ${employeeResponse.body().toString()}")
                            with(Dispatchers.Main) {
                                tvUserInfo.text = employeeResponse.body().toString()
                            }
                        } else{
                            Log.i("ECO","Employee Response ${employeeResponse.code()}")
                        }
                    } else {
                        Log.i("ECO","Role $role Id $id")
                    }
                }
            }
        }
    }
}