package br.edu.uea.ecopoints.screen.home.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.uea.ecopoints.databinding.FragmentPickUpRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickUpRequestFragment : Fragment() {
    private var _binding: FragmentPickUpRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickUpRequestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}