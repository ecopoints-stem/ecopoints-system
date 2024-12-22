package br.edu.uea.ecopoints.screen.home.driver.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.uea.ecopoints.databinding.FragmentClockInOutBinding
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestBinding
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestTodayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    // Pedidos de HOJE (DRIVER)
    private var _binding:FragmentPickUpDriverRequestTodayBinding? = null
    private val binding get() = _binding!!

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