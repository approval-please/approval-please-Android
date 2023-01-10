package com.umc.approval.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.umc.approval.R
import com.umc.approval.databinding.FragmentLoginBinding
import com.umc.approval.ui.activity.MainActivity
import java.util.regex.Pattern

/**
 * Login View
 * In here, User can social login or basic login to account
 * */
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        //main activity로 돌아가는 logic
        back_to_main_activity()

        //email text 초기화 logic
        init_email()

        //email 검증 logic
        validate_email()
        return view
    }

    /**
     * main activity로 이동
     * */
    private fun back_to_main_activity() {
        binding.backToMainActivity.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    /**
     * email 유효성 검사 후 유효한 이메일일 경우 password fragment로 이동
     * */
    private fun validate_email() {
        binding.emailLoginButton.setOnClickListener {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS

            val email = binding.email.text

            if (pattern.matcher(email).matches()) {
                binding.emailValid.isVisible = false
                Toast.makeText(requireContext(), "유효한 이메일입니다", Toast.LENGTH_SHORT).show()

                /**
                 * 유효한 email에 대해 회원 여부 확인후 view 이동
                 * */
                if (email.toString() == "cswcsm02@gmail.com") {
                    Toast.makeText(requireContext(), "회원입니다", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_passwordFragment)
                } else {
                    Toast.makeText(requireContext(), "회원가입이 필요합니다", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_joinFragment)
                }

            } else {
                binding.email.setBackgroundResource(R.drawable.login_activity_red_box)
                binding.validFail.isVisible = true
                binding.textRemove.isVisible = false
                binding.emailValid.isVisible = true
                Toast.makeText(requireContext(), "유효한 이메일아닙니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * x버튼 누르면 입력중인 내용 모두 삭제
     * */
    private fun init_email() {
        binding.textRemove.setOnClickListener {
            binding.emailValid.isVisible = false
            binding.email.text.clear()
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