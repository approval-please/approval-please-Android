package com.umc.approval.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.umc.approval.R
import com.umc.approval.data.dto.login.post.BasicLoginDto
import com.umc.approval.databinding.FragmentPasswordBinding
import com.umc.approval.ui.activity.MainActivity
import com.umc.approval.ui.viewmodel.login.BasicLoginViewModel
import com.umc.approval.util.BlackToast

/**
 * basic login password view
 * */
class PasswordFragment : Fragment() {

    private var _binding : FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    val get_email : PasswordFragmentArgs by navArgs()

    private val viewModel by viewModels<BasicLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        //login button click
        login()

        //back to loginFragment
        backToLoginFragment()

        //password clear
        init_password()

        //move to passwordResetFragment
        moveToPasswordResetFragment()

        //로그인 성공시 메인 화면으로 이동
        viewModel.success.observe(viewLifecycleOwner) {
            if (it == true) {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }, 300)
            }
        }

        return view
    }

    /**
     * x버튼 누르면 입력중인 내용 모두 삭제
     * */
    private fun init_password() {
        binding.textRemove.setOnClickListener {
            binding.password.text.clear()
        }
    }

    /**
     * back to loginFragment
     * */
    private fun backToLoginFragment() {
        binding.backToEmailLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_passwordFragment_to_loginFragment)
        }
    }

    /**
     * move to password_reset_fragmnet
     * */
    private fun moveToPasswordResetFragment() {
        binding.passwordForgot.setOnClickListener {

            val to_password_reset = PasswordFragmentDirections.actionPasswordFragmentToPasswordResetFragment(get_email.email)

            Navigation.findNavController(binding.root).navigate(to_password_reset)
        }
    }

    /**
     * 로그인 버튼 클릭시 패스워드가 일치하면 MainActivity, 일치하지 않으면 오류 발생
     * */
    private fun login() {
        binding.loginButton.setOnClickListener {
            val basicLoginDto = BasicLoginDto(get_email.email, binding.password.text.toString())
            viewModel.login(basicLoginDto)
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