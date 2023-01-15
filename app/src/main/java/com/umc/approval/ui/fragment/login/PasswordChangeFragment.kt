package com.umc.approval.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.umc.approval.R
import com.umc.approval.databinding.FragmentPasswordChangeBinding

/**
 * basic login password view
 * */
class PasswordChangeFragment : Fragment() {

    private var _binding : FragmentPasswordChangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPasswordChangeBinding.inflate(inflater, container, false)
        val view = binding.root

        //back to LoginFragment
        backToLoginFragment()

        return view
    }

    /**
     * LoginFragment로 이동
     * */
    private fun backToLoginFragment() {
        binding.backToLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_passwordChangeFragment_to_loginFragment)
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}