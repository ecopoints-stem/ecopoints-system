package br.edu.uea.ecopoints.screen.home.driver.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentEditProfileDriverBinding
import br.edu.uea.ecopoints.databinding.FragmentPickUpDriverRequestBinding
import br.edu.uea.ecopoints.screen.home.admin.HomeViewModel
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.EditProfileDriverViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileDriverFragment : Fragment() {
    private var _binding: FragmentEditProfileDriverBinding? = null
    private val binding get() = _binding!!

    private lateinit var edtName: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtCnh: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnUpdate: MaterialButton

    private val editDriverEmployeeViewModel: EditProfileDriverViewModel by viewModels()
    private val homeViewModel : HomeViewModel by activityViewModels()

    @Inject
    lateinit var shared: SharedPreferences

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