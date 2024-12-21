package br.edu.uea.ecopoints.screen.home.admin.fragment.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentAdminReportControlBinding
import br.edu.uea.ecopoints.domain.network.response.BarData
import br.edu.uea.ecopoints.screen.home.admin.HomeViewModel
import br.edu.uea.ecopoints.screen.home.admin.state.report.DefaultState
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.ReportViewModel
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.report.ControlViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class ControlFragment : Fragment() {
    private var _binding : FragmentAdminReportControlBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvCooperativeName: TextView
    private lateinit var barChartMaterials: BarChart
    private lateinit var btnReport: MaterialButton
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val controlViewModel : ControlViewModel by viewModels()

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val dateString: String =  now.format(formatter)
        controlViewModel.getBarData(dateString)
        controlViewModel.state.observe(viewLifecycleOwner) { state: DefaultState ->
            if(state.isProgressVisible){
                homeViewModel.state.value = HomeState.Loading
            } else{
                homeViewModel.state.value = HomeState.NotLoading
            }
            if(state.isErrorMessageVisible && state.errorResponseApi!=null){
                homeViewModel.state.value = HomeState.InconsistentInput(state.errorResponseApi!!,state.errorMessage!!)
            }
        }
        controlViewModel.barData.observe(viewLifecycleOwner){ barChartMaterials: BarData? ->
            if(barChartMaterials!=null){
                populateData(barChartMaterials)
                homeViewModel.state.value = HomeState.Success(barChartMaterials)
            }
        }
    }

    private fun populateData(data: BarData) {
        with(barChartMaterials){
            description.isEnabled = false
            setFitBars(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false
            legend.isEnabled = true
        }
        val entries = data.data.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }
        val dataSet = BarDataSet(entries, "Materiais")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f
        val barData = com.github.mikephil.charting.data.BarData(dataSet)
        barChartMaterials.data = barData
        barChartMaterials.xAxis.valueFormatter = object : ValueFormatter() {
            private val labels = data.data.keys.map { it.name }
            override fun getFormattedValue(value: Float): String {
                return labels.getOrNull(value.toInt()) ?: ""
            }
        }
        barChartMaterials.invalidate()
    }

    private fun setupListeners() {
        btnReport.setOnClickListener {
            val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            val dateString: String =  now.format(formatter)
            controlViewModel.generateControlReport(dateString)
        }
    }

    private fun setupView() {
        tvCooperativeName = binding.tvCooperativeName
        barChartMaterials = binding.bcHistogram
        btnReport = binding.btnGenerateReport
    }
}