package br.edu.uea.ecopoints.screen.home.admin.fragment.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentAdminReportControlBinding
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.ReportViewModel
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.report.ControlViewModel
import com.github.mikephil.charting.charts.BarChart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ControlFragment : Fragment() {
    private var _binding : FragmentAdminReportControlBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvCooperativeName: TextView
    private lateinit var barChartMaterials: BarChart
    private val controlViewModel : ControlViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReportControlBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {

    }

    private fun setupView() {
        tvCooperativeName = binding.tvCooperativeName
        barChartMaterials = binding.bcHistogram
    }
}