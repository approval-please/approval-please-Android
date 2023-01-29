package com.umc.approval.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.umc.approval.R
import com.umc.approval.data.dto.login.post.PasswordChangeDto
import com.umc.approval.databinding.FragmentPasswordResetBinding
import com.umc.approval.ui.viewmodel.login.PasswordChangeViewModel
import java.util.regex.Pattern

/**
 * basic login password view
 * */
class PasswordResetFragment : Fragment() {

    private var _binding : FragmentPasswordResetBinding? = null
    private val binding get() = _binding!!

    val get_email : PasswordResetFragmentArgs by navArgs()

    private val viewModel by viewModels<PasswordChangeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        val view = binding.root

        //login button click
        passwordChange()

        //back to PasswordFragment
        backToPasswordFragment()

        //password clear
        init_password()

        return view
    }

    /**
     * x버튼 누르면 입력중인 내용 모두 삭제
     * */
    private fun init_password() {
        binding.textRemove.setOnClickListener {
            binding.password.text.clear()
        }

        binding.retryTextRemove.setOnClickListener {
            binding.passwordRetry.text.clear()
        }
    }

    /**
     * PasswordFragment로 이동
     * */
    private fun backToPasswordFragment() {
        binding.backToEmailLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_passwordResetFragment_to_passwordFragment)
        }
    }

    /**
     * 로그인 버튼 클릭시 패스워드가 일치하면 MainActivity, 일치하지 않으면 오류 발생
     * */
    private fun passwordChange() {
        binding.passwordChangeButton.setOnClickListener {

            //패스워드가 유효성 검사를 성공한 경우
            if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$", binding.password.text)) {
                proper_password()

                //만약 재확인 패스워드와 일치하는 경우
                if (binding.password.text.toString() == binding.passwordRetry.text.toString()) {
                    proper_retry_password()
                    Navigation.findNavController(binding.root).navigate(R.id.action_passwordResetFragment_to_passwordChangeFragment)

                    viewModel.password_change(PasswordChangeDto(get_email.email, binding.password.text.toString()))
                } else {
                    not_proper_retry_password()
                }
            } else {
                //패스워드가 유효성 검사를 실패한 경우
                not_proper_password()
            }
        }
    }

    /**패스워드를 올바르게 입력한 경우*/
    private fun proper_password() {
        binding.passwordFail.isVisible = false
        binding.passwordFailMessage.isVisible = false
        binding.textRemove.isVisible = true
        binding.password.setBackgroundResource(R.drawable.login_activity_green_box)
    }

    /**패스워드를 올바르게 입력하지 않은 경우*/
    private fun not_proper_password() {
        binding.passwordFail.isVisible = true
        binding.passwordFailMessage.isVisible = true
        binding.textRemove.isVisible = false
        binding.password.setBackgroundResource(R.drawable.login_activity_red_box)
    }

    /**재확인 패스워드를 올바르게 입력한 경우*/
    private fun proper_retry_password() {
        binding.retryPasswordFail.isVisible = false
        binding.retryPasswordFailMessage.isVisible = false
        binding.retryTextRemove.isVisible = true
        binding.password.setBackgroundResource(R.drawable.login_activity_green_box)
    }

    /**재확인 패스워드를 올바르게 입력하지 않은 경우*/
    private fun not_proper_retry_password() {
        binding.retryPasswordFail.isVisible = true
        binding.retryPasswordFailMessage.isVisible = true
        binding.retryTextRemove.isVisible = false
        binding.password.setBackgroundResource(R.drawable.login_activity_green_box)
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}