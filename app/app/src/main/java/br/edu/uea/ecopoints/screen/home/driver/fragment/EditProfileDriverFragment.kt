package br.edu.uea.ecopoints.screen.home.driver.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.uea.ecopoints.databinding.FragmentEditProfileDriverBinding
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileDriverFragment : Fragment() {
    private var _binding: FragmentEditProfileDriverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentEditProfileDriverBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {

    }

    private fun setupView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}