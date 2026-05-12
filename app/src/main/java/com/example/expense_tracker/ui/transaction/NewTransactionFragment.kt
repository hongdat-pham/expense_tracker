package com.example.expense_tracker.ui.transaction

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentNewTransactionBinding

class NewTransactionFragment : Fragment(R.layout.fragment_new_transaction) {

    private var _binding: FragmentNewTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewTransactionBinding.bind(view)

        // 1. Nút Close/Back
        binding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Nút Save Transaction
        binding.btnSaveTransaction.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            if (amount.isNotEmpty()) {
                Toast.makeText(requireContext(), "Saved: $$amount", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Logic chọn Category sơ bộ (Ví dụ chọn Shopping)
        binding.categoryShopping.setOnClickListener {
            // Sau này sẽ xử lý đổi màu stroke để người dùng biết đã chọn
            Toast.makeText(requireContext(), "Selected: Shopping", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}