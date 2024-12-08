package br.edu.uea.ecopoints.screen.home.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.ecopoints.databinding.FragmentPickUpRequestBinding
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.screen.home.admin.fragment.adapter.PickUpAdapter
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.PickUpRequestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PickUpRequestFragment : Fragment() {
    private var _binding: FragmentPickUpRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PickUpAdapter
    private val pickupViewModel: PickUpRequestViewModel by viewModels()
    private lateinit var rc: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickUpRequestBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rc.adapter = adapter
        rc.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            pickupViewModel.pickups.collectLatest { paging: PagingData<PickUpRequest> ->
                adapter.submitData(paging)
            }
        }
    }

    private fun setupListeners() {

    }

    private fun setupView() {
        rc = binding.rvPickupItems
        adapter = PickUpAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}