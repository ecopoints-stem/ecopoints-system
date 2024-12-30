package br.edu.uea.ecopoints.screen.home.admin.fragment.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentAdminReportClientBinding
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.report.ClientViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ClientFragment :  Fragment() {
    private var _binding : FragmentAdminReportClientBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnStartDate: MaterialButton
    private lateinit var btnEndDate: MaterialButton
    private lateinit var btnGenerate: MaterialButton
    private lateinit var edtCnpj : TextInputEditText
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val clientViewModel: ClientViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReportClientBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        btnStartDate.setOnClickListener {
            showDatePicker { selectedDate ->
                btnStartDate.text = selectedDate
                if(btnEndDate.text.contains("/") && edtCnpj.text?.isNotBlank()==true){
                    try {
                        val start = LocalDate.parse(selectedDate, formatter)
                        val end = LocalDate.parse(btnEndDate.text.toString(),formatter)
                        if (start != null && end != null && start.isAfter(end)) {
                            Toast.makeText(requireContext(), "Data de início deve ser antes da data de fim", Toast.LENGTH_SHORT).show()
                        } else if(edtCnpj.text?.isNotBlank()==true){
                            Toast.makeText(requireContext(), "Campos de data validados, pode gerar", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Campos CNPJ em branco", Toast.LENGTH_SHORT).show()
                        }
                    } catch (ex: Exception){
                        Toast.makeText(requireContext(), "Erro ao validar datas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        btnEndDate.setOnClickListener {
            showDatePicker { selectedDate ->
                try {
                    if(btnStartDate.text.contains("/")){
                        val start = LocalDate.parse(btnStartDate.text.toString(), formatter)
                        val end = LocalDate.parse(selectedDate, formatter)
                        if (start != null && end != null && start.isAfter(end)) {
                            Toast.makeText(requireContext(), "Data de início deve ser antes da data de fim", Toast.LENGTH_SHORT).show()
                        } else if (edtCnpj.text?.isNotBlank()==true){
                            Toast.makeText(requireContext(), "Campos de data validados, pode gerar", Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(requireContext(), "Campos CNPJ em branco", Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(requireContext(), "Campo de data de início não preenchido", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception){
                    Toast.makeText(requireContext(), "Erro ao validar datas", Toast.LENGTH_SHORT).show()
                }
                btnEndDate.text = selectedDate
            }
        }
        btnGenerate.setOnClickListener {
            try {
                if(edtCnpj.text?.isNotBlank()==true){
                    if(btnStartDate.text!!.contains("/") && btnEndDate.text!!.contains("/")){
                        val startDate = LocalDate.parse(btnStartDate.text.toString(),formatter)
                        val endDate = LocalDate.parse(btnEndDate.text.toString(),formatter)
                        if(startDate.isAfter(endDate)){
                            Toast.makeText(requireContext(),"Intervalo entre datas inválido, favor verificar novamente",Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(requireContext(),"Campos validados, gerar documento excel", Toast.LENGTH_SHORT).show()
                            clientViewModel.emitExcelDocument(edtCnpj.text.toString(),btnStartDate.text.toString(), btnEndDate.text.toString())
                        }
                    } else {
                        Toast.makeText(requireContext(),"Campos de data de início ou término não preenchidos",Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(requireContext(),"CNPJ é campo obrigatório", Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception){
                Toast.makeText(requireContext(), "Erro ao validar datas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        btnStartDate = binding.btnStartDate
        btnEndDate = binding.btnEndDate
        edtCnpj = binding.edtCnpj
        btnGenerate = binding.btnGenerate
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.ENGLISH,"%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

}