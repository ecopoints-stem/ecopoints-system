package br.edu.uea.ecopoints.screen.home.driver.fragment

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestTodayBinding
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import br.edu.uea.ecopoints.screen.home.admin.HomeAdminActivity
import br.edu.uea.ecopoints.screen.home.driver.HomeDriverActivity
import br.edu.uea.ecopoints.screen.home.driver.fragment.recyclerview.today.PickUpTodayAdapter
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.PickUpRequestTodayViewModel
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.factory.PickUpRequestTodayViewModelFactory
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    // Pedidos de HOJE (DRIVER)
    private var _binding:FragmentPickUpDriverRequestTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var rc: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: PickUpTodayAdapter

    @Inject lateinit var ecoApi: EcoApi
    @Inject lateinit var mapper: ObjectMapper
    @Inject lateinit var shared: SharedPreferences
    private val todayViewModel by viewModels<PickUpRequestTodayViewModel> {
        val driverId = shared.getLong("id",-1L)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val todayString = LocalDate.now().format(formatter)
        PickUpRequestTodayViewModelFactory(
            ecoApi, shared, mapper,
            driverId, todayString, listOf("ACCEPTED","COMPLETED","NOT_COMPLETED")
        )
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentPickUpDriverRequestTodayBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PickUpTodayAdapter { pickUpRequest ->
            showStatusDialog(pickUpRequest)
        }
        rc.adapter = adapter
        rc.itemAnimator = null
        rc.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            todayViewModel.pickups.collectLatest { paging: PagingData<PickUpRequest> ->
                adapter.submitData(paging)
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
        val activityBinding = (requireActivity() as HomeDriverActivity).binding
        val bottomNavigationView = activityBinding.bottomNavigationDriver
        bottomNavigationView.viewTreeObserver?.addOnGlobalLayoutListener {
            val bottomNavHeight = bottomNavigationView.height

            rc.setPadding(0, 0, 0, bottomNavHeight)
            rc.clipToPadding = false
        }
    }

    private fun setupListeners() {

    }

    private fun showStatusDialog(pickUpRequest: PickUpRequest) {
        pickUpRequest.status
        AlertDialog
            .Builder(requireContext())
            .setTitle("Atualizar status do Pedido de Entrega")
            .setPositiveButton("concluido"){ dialog: DialogInterface, _: Int ->
                updatePickUp(pickUpRequest.id, pickUpRequest.status, PickupRequestStatus.COMPLETED)
                dialog.dismiss()
            }.setNegativeButton("não realizado"){ dialog: DialogInterface, _: Int ->
                updatePickUp(pickUpRequest.id, pickUpRequest.status, PickupRequestStatus.NOT_COMPLETED)
                dialog.dismiss()
            }.show()
    }

    private fun updatePickUp(pickUpId: Long, oldStatus: PickupRequestStatus, newStatus: PickupRequestStatus){
        if(oldStatus==PickupRequestStatus.COMPLETED && newStatus!=oldStatus){
            Toast.makeText(requireContext(),"Operação Inválida !!",Toast.LENGTH_SHORT).show()
        }else{
            if(
                (oldStatus==PickupRequestStatus.ACCEPTED && newStatus==PickupRequestStatus.COMPLETED) ||
                (oldStatus==PickupRequestStatus.ACCEPTED && newStatus==PickupRequestStatus.NOT_COMPLETED)
            ){
                lifecycleScope.launch {
                    val pickUp = todayViewModel.updatePickUpStatus(pickUpId, newStatus)
                    if(pickUp!=null){
                        adapter.refresh()
                        Toast.makeText(requireContext(),"Atualizou com sucesso!!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"Erro ao atualizar status",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Operação Inválida !!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        rc = binding.rvPickupTodayItems
        swipeRefreshLayout = binding.swpTodayItems
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}