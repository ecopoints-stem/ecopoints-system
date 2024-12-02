package br.edu.uea.ecopoints.screen.home.employee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.uea.ecopoints.databinding.FragmentEditProfileEmployeeBinding

class EditProfileEmployeeFragment : Fragment() {
    private var _binding: FragmentEditProfileEmployeeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentEditProfileEmployeeBinding.inflate(inflater,group, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}