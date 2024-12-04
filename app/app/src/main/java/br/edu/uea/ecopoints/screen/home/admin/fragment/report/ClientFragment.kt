package br.edu.uea.ecopoints.screen.home.admin.fragment.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.uea.ecopoints.databinding.FragmentAdminReportClientBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientFragment :  Fragment() {
    private var _binding : FragmentAdminReportClientBinding? = null
    private val binding get() = _binding!!

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

    }

    private fun setupView() {

    }
}