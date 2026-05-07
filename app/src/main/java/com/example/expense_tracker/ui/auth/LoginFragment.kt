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
import com.example.expense_tracker.databinding.FragmentLoginBinding
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Auto-fill if coming from Register (via arguments)
        arguments?.let { args ->
            val email = args.getString("prefill_email")
            val password = args.getString("prefill_password")
            if (!email.isNullOrEmpty()) binding.etEmail.setText(email)
            if (!password.isNullOrEmpty()) binding.etPassword.setText(password)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observeLoginState()
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthResult.Loading -> {
                        binding.btnLogin.isEnabled = false
                    }
                    is AuthViewModel.AuthResult.Success -> {
                        binding.btnLogin.isEnabled = true
                        // Navigate to main graph, clear auth back stack
                        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                        requireActivity().finish()
                    }
                    is AuthViewModel.AuthResult.Error -> {
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetLoginState()
                    }
                    is AuthViewModel.AuthResult.Idle -> {
                        binding.btnLogin.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}