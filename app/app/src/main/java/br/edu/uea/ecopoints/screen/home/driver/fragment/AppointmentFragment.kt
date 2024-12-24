package br.edu.uea.ecopoints.screen.home.driver.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.databinding.FragmentClockInOutBinding
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestBinding
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestTodayBinding
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.PickUpRequestTodayViewModel
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.factory.PickUpRequestTodayViewModelFactory
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    // Pedidos de HOJE (DRIVER)
    private var _binding:FragmentPickUpDriverRequestTodayBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var ecoApi: EcoApi
    @Inject lateinit var mapper: ObjectMapper
    @Inject lateinit var shared: SharedPreferences
    private val todayViewModel by viewModels<PickUpRequestTodayViewModel> {
        val driverId = shared.getLong("id",-1L)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val todayString = LocalDate.now().format(formatter)
        PickUpRequestTodayViewModelFactory(
            ecoApi, shared, mapper,
            driverId, todayString, listOf("ACCEPTED","COMPLETED")
        )
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentPickUpDriverRequestTodayBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {

    }

    private fun setupView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}