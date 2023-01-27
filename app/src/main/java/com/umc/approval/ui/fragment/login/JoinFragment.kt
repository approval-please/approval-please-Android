package com.umc.approval.ui.fragment.login

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.umc.approval.R
import com.umc.approval.data.dto.login.post.PhoneAuthDto
import com.umc.approval.databinding.FragmentJoinBinding
import com.umc.approval.ui.viewmodel.login.JoinViewModel
import java.util.regex.Pattern

class JoinFragment : Fragment() {
    private var _binding : FragmentJoinBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<JoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        val view = binding.root

        //phone validation
        phone_validation()

        //가입 눌렀을때 로직
        validation()

        //logic about terms
        checkbox_click()

        //init value(nickname, password, password_retry, phone, auth)
        init_value()

        //email 입력칸으로 이동
        back_to_login()

        return view
    }

    /**login fragment로 이동*/
    private fun back_to_login() {
        binding.backToEmailLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_joinFragment_to_loginFragment)
        }
    }

    /**
     * 전체 체크박스 눌렀을때 로직
     * */
    private fun checkbox_click() {
        /**
         * 전체 체크박스 눌렀을때 로직
         * */
        binding.checkboxAll.setOnClickListener {

            val checkbox1 = binding.checkbox1.isChecked
            val checkbox2 = binding.checkbox2.isChecked
            val checkbox3 = binding.checkbox3.isChecked

            /**
             * if 모두 체크되어있으면 체크 해제
             * else 하나라도 체크가 안되어있으면 모두 체크
             * */
            if (checkbox1 && checkbox2 && checkbox3) {
                binding.checkboxAll.isChecked = false
                binding.checkbox1.isChecked = false
                binding.checkbox2.isChecked = false
                binding.checkbox3.isChecked = false
            } else {
                binding.checkboxAll.isChecked = true
                binding.checkbox1.isChecked = true
                binding.checkbox2.isChecked = true
                binding.checkbox3.isChecked = true
            }
        }
    }

    /**
     * phone 번호 유효성 검사 후 인증 요청 및 인증 확인
     * */
    private fun phone_validation() {
        /**인증 요청 눌렀을때 로직*/
        binding.authButton.setOnClickListener {

            /**휴대폰 인증이 정상적인 경우*/
            if (Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", binding.phone.text)) {

                viewModel.phone_request(binding.phone.text.toString())
            }
        }

        /**인증번호 확인 로직*/
        binding.authCheckButton.setOnClickListener {

            val phoneAuthDto = PhoneAuthDto(binding.phone.text.toString(), binding.auth.text.toString())
            viewModel.phone_auth_request(phoneAuthDto)

            //viewmodel livedata
            viewModel.phone_auth.observe(viewLifecycleOwner) {

                if (it == 1) {//올바른 번호이면
                    proper_phone_auth()
                    proper_phone()
                } else if (it == 2) { //올바른 번호가 아니면
                    not_proper_phone_auth()
                    not_proper_phone()
                    dialog()
                }
            }
        }
    }

    /**회원가입 유효성 체크*/
    private fun validation() {

        /**회원가입 체크 체크*/
        binding.join.setOnClickListener {

            /**닉네임 체크*/
            if (binding.nickname.text.toString() == "" || binding.nickname.text.isEmpty()) {
                //닉네임이 올바르게 입력되지 않음
                not_proper_nickname()
            } else {
                //닉네임이 올바르게 입력됨
                proper_nickname()
            }

            /**비밀번호 로직*/
            if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$", binding.password.text)) {
                //패스워드가 올바르게 입력되었지만 이미 존재하는 회원인지 확인하는 로직
                proper_password()

                /**재확인 비밀먼호 로직* */
                if (binding.password.text.toString() == binding.passwordRetry.text.toString()) {
                    //비밀번호와 재확인 비밀번호가 일치함
                    proper_retry_password()
                } else {
                    //비밀번호와 재확인 비밀번호가 일치하지 않음
                    not_proper_retry_password()
                }
            } else {
                //비밀번호가 올바르게 입력되지 않음
                not_proper_password()
            }

            /**인증이 된 상태인지 확인해서 인증 요구* */
            if (binding.phoneSuccess.isVisible && binding.authSuccess.isVisible) {
                proper_phone()
                proper_phone_auth()
            } else {
                not_proper_phone()
            }

            /**필수 약관에 동의한 상태인지 확인*/
            if (!(binding.checkbox1.isChecked && binding.checkbox2.isChecked)) {
                Toast.makeText(requireContext(), "인증약관에 체크해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**init value(nickname, password, password_retry, phone, auth)*/
    private fun init_value() {

        binding.textRemove.setOnClickListener {
            binding.nickname.text.clear()
        }

        binding.passwordTextRemove.setOnClickListener {
            binding.password.text.clear()
        }

        binding.phoneTextRemove.setOnClickListener {
            binding.phone.text.clear()
        }

        binding.authTextRemove.setOnClickListener {
            binding.auth.text.clear()
        }

        binding.passwordRetryTextRemove.setOnClickListener {
            binding.passwordRetry.text.clear()
        }
    }

    /**닉네임을 올바르게 입력한 경우*/
    private fun proper_nickname() {
        binding.nicknameFail.isVisible = false
        binding.nicknameSuccess.isVisible = true
        binding.nicknameValid.isVisible = false
        binding.textRemove.isVisible = false
        binding.nickname.setBackgroundResource(R.drawable.login_activity_green_box)
        Toast.makeText(requireContext(), "닉네임을 올바른 형식입니다", Toast.LENGTH_SHORT).show()
    }

    /**닉네임을 올바르게 입력하지 않은 경우*/
    private fun not_proper_nickname() {
        binding.nicknameFail.isVisible = true
        binding.nicknameSuccess.isVisible = false
        binding.nicknameValid.isVisible = true
        binding.textRemove.isVisible = false
        binding.nickname.setBackgroundResource(R.drawable.login_activity_red_box)
        Toast.makeText(requireContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
    }

    /**패스워드를 올바르게 입력한 경우*/
    private fun proper_password() {
        binding.passwordSuccess.isVisible = true
        binding.passwordFail.isVisible = false
        binding.passwordValid.isVisible = false
        binding.passwordTextRemove.isVisible = false
        binding.password.setBackgroundResource(R.drawable.login_activity_green_box)
        Toast.makeText(requireContext(), "비밀번호가 올바른 형식입니다", Toast.LENGTH_SHORT).show()
    }

    /**패스워드를 올바르게 입력하지 않은 경우*/
    private fun not_proper_password() {
        binding.passwordTextRemove.isVisible = false
        binding.password.setBackgroundResource(R.drawable.login_activity_red_box)
        binding.passwordSuccess.isVisible = false
        binding.passwordFail.isVisible = true
        binding.passwordValid.isVisible = true
        Toast.makeText(requireContext(), "비밀번호가 잘못된 형식입니다", Toast.LENGTH_SHORT).show()
    }

    /**재확인 패스워드를 올바르게 입력한 경우*/
    private fun proper_retry_password() {
        binding.passwordRetry.setBackgroundResource(R.drawable.login_activity_green_box)
        binding.passwordRetrySuccess.isVisible = true
        binding.passwordRetryFail.isVisible = false
        binding.passwordRetryValid.isVisible = false
        binding.passwordRetryTextRemove.isVisible = false
        Toast.makeText(requireContext(), "비밀번호가 서로 일치합니다", Toast.LENGTH_SHORT).show()
    }

    /**재확인 패스워드를 올바르게 입력하지 않은 경우*/
    private fun not_proper_retry_password() {
        binding.passwordRetry.setBackgroundResource(R.drawable.login_activity_white_box)
        binding.passwordRetrySuccess.isVisible = false
        binding.passwordRetryFail.isVisible = false
        binding.passwordRetryValid.isVisible = false
        binding.passwordRetryTextRemove.isVisible = true
        Toast.makeText(requireContext(), "비밀번호가 서로 일치하지 않습니다", Toast.LENGTH_SHORT).show()
    }

    /**폰 번호를 올바르게 입력한 경우*/
    private fun proper_phone() {
        binding.phoneFail.isVisible = false
        binding.phoneSuccess.isVisible = true
        binding.phoneTextRemove.isVisible = false
        binding.phoneValid.isVisible = false
        binding.phone.setBackgroundResource(R.drawable.login_activity_green_box)
        Toast.makeText(requireContext(), "휴대번호가 올바르게 입력되었습니다", Toast.LENGTH_SHORT).show()
    }

    /**폰 번호를 올바르게 입력하지 않은 경우*/
    private fun not_proper_phone() {
        binding.phoneFail.isVisible = true
        binding.phoneSuccess.isVisible = false
        binding.phoneTextRemove.isVisible = false
        binding.phoneValid.isVisible = true
        binding.phone.setBackgroundResource(R.drawable.login_activity_red_box)
        Toast.makeText(requireContext(), "휴대번호가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show()
    }

    /**인증번호가 올바르게 입력된 경우*/
    private fun proper_phone_auth() {
        binding.authSuccess.isVisible = true
        binding.authFail.isVisible = false
        binding.authValid.isVisible = false
        binding.authTextRemove.isVisible = false
        binding.auth.setBackgroundResource(R.drawable.login_activity_green_box)
    }

    /**폰 번호를 올바르게 입력하지 않은 경우*/
    private fun not_proper_phone_auth() {
        binding.authSuccess.isVisible = false
        binding.authFail.isVisible = true
        binding.authValid.isVisible = true
        binding.authTextRemove.isVisible = true
        binding.auth.setBackgroundResource(R.drawable.login_activity_red_box)
    }

    /**dialog를 보여주는 메소드*/
    private fun dialog() {
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.join_fragment_dialog, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialog)

        val alertDialog = builder.show()

        //dialog의 view Component 접근
        val dialog_cancel = alertDialog.findViewById<ImageView>(R.id.back)
        val back_to_login = alertDialog.findViewById<ImageView>(R.id.back_fragment)

        dialog_cancel.setOnClickListener {
            alertDialog.cancel()
            viewModel.phone_auth_to_zero()
        }

        back_to_login.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_joinFragment_to_loginFragment)
            alertDialog.cancel()
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