package com.example.expense_tracker.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button → pop to LoginFragment
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            viewModel.register(fullName, email, password)
        }

        observeRegisterState()
        observeCredentials()
    }

    private fun observeRegisterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthResult.Loading -> {
                        binding.btnRegister.isEnabled = false
                    }
                    is AuthViewModel.AuthResult.Success -> {
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(requireContext(), "Account created! Please sign in.", Toast.LENGTH_SHORT).show()
                        viewModel.resetRegisterState()
                        // Navigation happens after credentials are emitted (see observeCredentials)
                    }
                    is AuthViewModel.AuthResult.Error -> {
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetRegisterState()
                    }
                    is AuthViewModel.AuthResult.Idle -> {
                        binding.btnRegister.isEnabled = true
                    }
                }
            }
        }
    }

    private fun observeCredentials() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registeredCredentials.collect { creds ->
                // Navigate back to Login and pass credentials for auto-fill
                val args = Bundle().apply {
                    putString("prefill_email", creds.email)
                    putString("prefill_password", creds.password)
                }
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment, args)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}