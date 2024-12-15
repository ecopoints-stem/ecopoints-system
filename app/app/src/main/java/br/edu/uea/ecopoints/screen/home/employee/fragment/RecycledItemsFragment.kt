package br.edu.uea.ecopoints.screen.home.employee.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.DialogAddNewSeparatedMaterialBinding
import br.edu.uea.ecopoints.databinding.FragmentRecycledItemsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecycledItemsFragment : Fragment() {
    private var _binding: FragmentRecycledItemsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fabAddNewSeparatedMaterial: FloatingActionButton
    private lateinit var rcSeparatedMaterial: RecyclerView
    private lateinit var swpRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentRecycledItemsBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
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