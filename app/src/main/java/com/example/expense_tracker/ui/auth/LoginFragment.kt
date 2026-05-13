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
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.data.local.entity.UserEntity
import com.example.expense_tracker.utils.MockData
import kotlinx.coroutines.launch
import java.security.MessageDigest

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

        // CHỈ CẦN THÊM ĐOẠN NÀY
        binding.btnCreateDemoAccount.setOnClickListener {
            createDemoAccount()
        }

        observeLoginState()
    }

    private fun createDemoAccount() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val existingUser = db.userDao().getUserByEmail("demo@gmail.com")

            if (existingUser == null) {
                val hashedPassword = sha256("123456")
                val userId = db.userDao().insertUser(
                    UserEntity(
                        fullName = "Demo User",
                        email = "demo@gmail.com",
                        passwordHash = hashedPassword
                    )
                )

                if (userId != -1L) {
                    // Tạo account
                    db.accountDao().insertAccount(
                        AccountEntity(
                            userId = userId,
                            name = "Main Wallet",
                            lastFourDigits = "8888",
                            type = AccountType.SAVING,
                            balance = 15000000.0,
                            isActive = true
                        )
                    )

                    // Tạo transactions
                    val mockTransactions = MockData.transactions(userId, userId)
                    db.transactionDao().insertAll(mockTransactions)

                    binding.etEmail.setText("demo@gmail.com")
                    binding.etPassword.setText("123456")
                    Toast.makeText(requireContext(), "Demo account created! Tap Login.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to create demo account", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.etEmail.setText("demo@gmail.com")
                binding.etPassword.setText("123456")
                Toast.makeText(requireContext(), "Demo account exists! Tap Login.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthResult.Loading -> {
                        binding.btnLogin.isEnabled = false
                        binding.btnCreateDemoAccount.isEnabled = false
                    }
                    is AuthViewModel.AuthResult.Success -> {
                        binding.btnLogin.isEnabled = true
                        binding.btnCreateDemoAccount.isEnabled = true
                        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                        requireActivity().finish()
                    }
                    is AuthViewModel.AuthResult.Error -> {
                        binding.btnLogin.isEnabled = true
                        binding.btnCreateDemoAccount.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetLoginState()
                    }
                    is AuthViewModel.AuthResult.Idle -> {
                        binding.btnLogin.isEnabled = true
                        binding.btnCreateDemoAccount.isEnabled = true
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