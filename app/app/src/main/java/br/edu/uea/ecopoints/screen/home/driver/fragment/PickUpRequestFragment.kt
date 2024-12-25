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
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestBinding
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import br.edu.uea.ecopoints.screen.home.driver.fragment.recyclerview.today.PickUpTodayAdapter
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.PickUpRequestOthersViewModel
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.factory.PickUpRequestOthersViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PickUpRequestFragment : Fragment() {
    private var _binding: FragmentPickUpDriverRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var rc: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: PickUpTodayAdapter

    @Inject lateinit var ecoApi: EcoApi
    @Inject lateinit var shared: SharedPreferences

    private val requestViewModel by viewModels<PickUpRequestOthersViewModel> {
        val driverId = shared.getLong("id",-1L)
        PickUpRequestOthersViewModelFactory(
            ecoApi, driverId, listOf("ACCEPTED","REJECTED","IN_PROGRESS")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickUpDriverRequestBinding.inflate(inflater,container,false)
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
        rc.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            requestViewModel.pickups.collectLatest { paging: PagingData<PickUpRequest> ->
                adapter.submitData(paging)
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupView() {
        rc = binding.rvPickupRequestItems
        swipeRefreshLayout = binding.swpRequestItems
    }

    private fun setupListeners() {

    }

    private fun showStatusDialog(pickUpRequest: PickUpRequest) {
        AlertDialog
            .Builder(requireContext())
            .setTitle("Atualizar status do Pedido de Entrega")
            .setPositiveButton("aceitar"){ dialog: DialogInterface, _: Int ->
                updatePickUp(pickUpRequest.id, pickUpRequest.status, PickupRequestStatus.ACCEPTED)
                dialog.dismiss()
            }.setNegativeButton("rejeitar"){ dialog: DialogInterface, _: Int ->
                updatePickUp(pickUpRequest.id, pickUpRequest.status, PickupRequestStatus.REJECTED)
                dialog.dismiss()
            }.show()
    }

    private fun updatePickUp(pickUpId: Long, oldStatus: PickupRequestStatus, newStatus: PickupRequestStatus) {
        // Se tá IN_PROGRESS, então ele pode ir para
        // ACCEPTED ou REJECTED
        // SE tá em REJECTED, ele pode migrar REJECTED
        // foda-se
        if(oldStatus==newStatus){
            Toast.makeText(requireContext(),"Mesmo status, não executando operação", Toast.LENGTH_SHORT).show()
        } else{
            lifecycleScope.launch {
                val pickUp = requestViewModel.updatePickUpStatus(pickUpId, newStatus)
                if(pickUp!=null){
                    adapter.refresh()
                    Toast.makeText(requireContext(),"Atualizou com sucesso!!", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(requireContext(),"Erro ao atualizar status",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}