package br.edu.uea.ecopoints.screen.home.employee.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.DialogAddNewSeparatedMaterialBinding
import br.edu.uea.ecopoints.databinding.FragmentRecycledItemsBinding
import br.edu.uea.ecopoints.domain.entity.SeparatedMaterial
import br.edu.uea.ecopoints.screen.home.admin.HomeAdminActivity
import br.edu.uea.ecopoints.screen.home.admin.HomeViewModel
import br.edu.uea.ecopoints.screen.home.employee.HomeEmployeeActivity
import br.edu.uea.ecopoints.screen.home.employee.HomeEmployeeViewModel
import br.edu.uea.ecopoints.screen.home.employee.fragment.recyclerview.SeparatedMaterialAdapter
import br.edu.uea.ecopoints.screen.home.employee.viewmodel.RecycledItemsViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RecycledItemsFragment : Fragment() {
    private var _binding: FragmentRecycledItemsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fabAddNewSeparatedMaterial: FloatingActionButton
    private lateinit var rcSeparatedMaterial: RecyclerView
    private lateinit var swpRefreshLayout: SwipeRefreshLayout
    private val adapter = SeparatedMaterialAdapter()

    private val homeViewModel : HomeViewModel by activityViewModels()
    private val recycledItemsViewModel: RecycledItemsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentRecycledItemsBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcSeparatedMaterial.adapter = adapter
        rcSeparatedMaterial.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            recycledItemsViewModel.recycledMaterials.collectLatest { paging: PagingData<SeparatedMaterial> ->
                adapter.submitData(paging)
            }
        }
        recycledItemsViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isErrorMessageVisible) {
                homeViewModel.state.value =
                    HomeState.Failed(state.errorDetails, state.errorMessage ?: "Erro")
            } else {
                homeViewModel.state.value = HomeState.Success("Deu certo")
            }
            if (state.isProgressVisible) {
                homeViewModel.state.value = HomeState.Loading
            } else{
                homeViewModel.state.value = HomeState.Success("Deu certo")
            }
        }
        swpRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            swpRefreshLayout.isRefreshing = false
        }
        val activityBinding = (requireActivity() as HomeEmployeeActivity).binding
        val bottomNavigationView = activityBinding.bottomNavigationEmployee
        bottomNavigationView.viewTreeObserver?.addOnGlobalLayoutListener {
            val bottomNavHeight = bottomNavigationView.height

            rcSeparatedMaterial.setPadding(0, 0, 0, bottomNavHeight)
            rcSeparatedMaterial.clipToPadding = false
        }
    }

    private fun setupView() {
        fabAddNewSeparatedMaterial = binding.fabAdd
        rcSeparatedMaterial = binding.rvRecycledItems
        swpRefreshLayout = binding.swpSeparatedMaterialItems
    }

    private fun setupListeners() {
        fabAddNewSeparatedMaterial.setOnClickListener{
            val dialogBinding = DialogAddNewSeparatedMaterialBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            val dialog = AlertDialog
                .Builder(requireContext())
                .setView(dialogBinding.root)
                .setTitle("Novo")
                .setNegativeButton("Cancelar") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.setPositiveButton("Salvar"){ dialogInterface, _ ->
                    if(
                        dialogBinding.edtQuantity.text?.isBlank() == true ||
                        dialogBinding.edtMaterialName.text?.isBlank() == true
                    ) {
                        Toast.makeText(requireContext(),"Campos vazios", Toast.LENGTH_SHORT).show()
                        dialogInterface.dismiss()
                    } else{
                        val nowDate = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        recycledItemsViewModel.createNewSeparatedMaterial(
                            lldate = nowDate.format(formatter),
                            materialName = dialogBinding.edtMaterialName.text?.toString() ?: "",
                            quantity = dialogBinding.edtQuantity.text?.toString()?.toDouble() ?: 0.0
                        )
                        Toast.makeText(requireContext(),"Salvou",Toast.LENGTH_SHORT).show()
                    }
                }.create()
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}