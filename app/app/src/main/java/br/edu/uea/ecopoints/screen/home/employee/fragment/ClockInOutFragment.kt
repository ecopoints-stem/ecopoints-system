package br.edu.uea.ecopoints.screen.home.employee.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.databinding.FragmentClockInOutBinding
import br.edu.uea.ecopoints.domain.entity.Employee
import br.edu.uea.ecopoints.domain.entity.enums.AttendanceRecordStatus
import br.edu.uea.ecopoints.domain.network.request.AttendanceRecordRegister
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ClockInOutFragment : Fragment() {
    private var _binding: FragmentClockInOutBinding? = null
    private val binding get() = _binding!!

    private lateinit var clockIn: ImageView
    private lateinit var clockOut: ImageView

    @Inject
    lateinit var ecoApi: EcoApi
    @Inject
    lateinit var shared: SharedPreferences

    @Inject
    lateinit var mapper: ObjectMapper

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val hrFormatter : DateTimeFormatter = DateTimeFormatter.ISO_TIME

    private var workDate: LocalDate? = null
    private var entryTime: LocalTime? = null
    private var exitTime: LocalTime? = null
    private var cooperativeId: Long? = null
    private var workStatusDay: AttendanceRecordStatus? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentClockInOutBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeId: Long = shared.getLong("id",-1L)
        lifecycleScope.launch (Dispatchers.IO){
            val employeeResponse = ecoApi.findEmployeeById(employeeId)
            if(employeeResponse.isSuccessful){
                Log.i("ECO","Recuperou employee do id $employeeId")
                employeeResponse.body()?.let { employee: Employee ->
                    with(shared.edit()){
                        putLong("cooperativeId",employee.cooperativeId ?: -1L)
                        commit()
                    }
                }
            } else {
                Log.i("ECO","erro na requisição ${employeeResponse.code()}")
            }
        }
    }

    private fun setupListeners() {
        clockIn.setOnClickListener {
            if(isValidClockRequest(true)){
                workDate = LocalDate.now()
                entryTime = LocalTime.now()
                exitTime = null
                val status: AttendanceRecordStatus = AttendanceRecordStatus.PRESENT
                val cooperativeId: Long = shared.getLong("cooperativeId",-1L)
                val employeeId: Long = shared.getLong("id",-1L)

                val hrRequest = AttendanceRecordRegister(
                    personDate = workDate!!,
                    entryTime = entryTime!!,
                    exitTime = exitTime,
                    status = status,
                    cooperativeId = cooperativeId
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    runCatching {
                        ecoApi.clockInClockOut(employeeId,hrRequest)
                    }.onSuccess { response ->
                        if(response.isSuccessful){
                            val attendanceRecord = response.body()
                            attendanceRecord?.run {
                                val editor = shared.edit()
                                editor.putString("workDate",dateFormatter.format(pDate))
                                editor.putString("entryTime",hrFormatter.format(entryTime))
                                editor.apply()
                                withContext(Dispatchers.Main){
                                    Toast.makeText(requireContext(),"Operação realizada",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else {
                            val errorBodyString = response.errorBody()?.string()
                            Log.e("ECO","Error Body $errorBodyString")
                            errorBodyString?.let { details ->
                                try {
                                    val exceptionDetails = mapper.readValue(details, ExceptionDetails::class.java)
                                    // mandar isso para a tela do usuário
                                    withContext(Dispatchers.Main){
                                        Toast.makeText(requireContext(),"Erro ${exceptionDetails.status} ${exceptionDetails.title}",Toast.LENGTH_SHORT).show()
                                    }
                                } catch (ex: Exception){
                                    Log.e("ECO",ex.message ?: "Erro em deserializar body")
                                }
                            }
                        }
                    }.onFailure { error ->
                        withContext(Dispatchers.Main){
                            Toast.makeText(requireContext(),"Erro ${error.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(),"Operação de entrada no trabalho inválida",Toast.LENGTH_LONG).show()
            }
        }
        clockOut.setOnClickListener {
            if (isValidClockRequest(false)){
                exitTime = LocalTime.now()
                val status: AttendanceRecordStatus = AttendanceRecordStatus.PRESENT
                val cooperativeId: Long = shared.getLong("cooperativeId",-1L)
                val employeeId: Long = shared.getLong("id",-1L)

                val hrRequest = AttendanceRecordRegister(
                    personDate = workDate!!,
                    entryTime = entryTime!!,
                    exitTime = exitTime,
                    status = status,
                    cooperativeId = cooperativeId
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    runCatching {
                        ecoApi.clockInClockOut(employeeId,hrRequest)
                    }.onSuccess { response ->
                        if(response.isSuccessful){
                            val editor = shared.edit()
                            editor.putString("workDate",null)
                            editor.putString("entryTime",null)
                            editor.apply()
                            withContext(Dispatchers.Main){
                                Toast.makeText(requireContext(),"Operação realizada",Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            val errorBodyString = response.errorBody()?.string()
                            Log.e("ECO","Error Body $errorBodyString")
                            errorBodyString?.let { details ->
                                try {
                                    val exceptionDetails = mapper.readValue(details, ExceptionDetails::class.java)
                                    // mandar isso para a tela do usuário
                                    withContext(Dispatchers.Main){
                                        Toast.makeText(requireContext(),"Erro ${exceptionDetails.status} ${exceptionDetails.title}",Toast.LENGTH_SHORT).show()
                                    }
                                } catch (ex: Exception){
                                    Log.e("ECO",ex.message ?: "Erro em deserializar body")
                                }
                            }
                        }
                    }.onFailure { error ->
                        withContext(Dispatchers.Main){
                            Toast.makeText(requireContext(),"Erro ${error.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(),"Operação de saída no trabalho inválida",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidClockRequest(isClockIn: Boolean) : Boolean{
        var isValid: Boolean = true

        val workDateString : String = shared.getString("workDate","") ?: ""
        val entryTimeString : String = shared.getString("entryTime","") ?: ""
        val cooperativeIdString: Long = shared.getLong("cooperativeId",-1L)

        if((workDateString.isEmpty() && entryTimeString.isEmpty()) && !isClockIn){
            isValid = false
        } else if(isClockIn && cooperativeIdString==-1L){
            isValid = false
        }

        if(workDateString.isNotEmpty()){
            workDate = LocalDate.parse(workDateString,dateFormatter)
        }
        if(entryTimeString.isNotEmpty()){
            entryTime = LocalTime.parse(entryTimeString,hrFormatter)
        }

        return isValid
    }

    private fun setupView() {
        clockIn = binding.ivClockIn
        clockOut = binding.ivClockOut
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}