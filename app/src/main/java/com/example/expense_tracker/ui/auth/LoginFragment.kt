package com.example.expense_tracker.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentLoginBinding
import com.example.expense_tracker.utils.SharedPrefsHelper

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nếu đã đăng nhập thì vào thẳng MainActivity với nav_main
        val prefs = SharedPrefsHelper.getInstance(requireContext())
        if (prefs.isLoggedIn()) {
            navigateToMain()
            return
        }

        // Nhận email từ RegisterFragment sau khi đăng ký thành công
        viewModel.registeredEmail.observe(viewLifecycleOwner) { email ->
            binding.etEmail.setText(email)
            binding.etPassword.requestFocus()
        }

        // Observe kết quả login
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Success -> navigateToMain()
                is AuthResult.Error -> Toast.makeText(
                    requireContext(),
                    result.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun navigateToMain() {
        // Switch sang nav_main bằng cách thay navGraph
        val navHostFragment = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_main)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}