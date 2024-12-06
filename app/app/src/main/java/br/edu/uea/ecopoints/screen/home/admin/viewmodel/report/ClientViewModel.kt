package br.edu.uea.ecopoints.screen.home.admin.viewmodel.report

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor() : ViewModel() {

    fun emitExcelDocument(cnpj: String, startDate: LocalDate, endDate: LocalDate) {
        
    }
}