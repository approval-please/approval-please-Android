package com.umc.approval.ui.fragment.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.umc.approval.R
import com.umc.approval.databinding.FragmentJoinBinding
import java.util.regex.Pattern

class JoinFragment : Fragment() {
    private var _binding : FragmentJoinBinding? = null
    private val binding get() = _binding!!

    var authState = false

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
        click_button()
        return view
    }

    /**
     * 전체 체크박스 눌렀을때 로직
     * */
    private fun click_button() {
        /**
         * 전체 체크박스 눌렀을때 로직
         * */
        binding.checkboxAll.setOnClickListener {

            //각 체크 파악 변수
            val checked_1 = binding.checkbox1.isChecked
            val checked_2 = binding.checkbox2.isChecked
            val checked_3 = binding.checkbox3.isChecked

            /**
             * if 모두 체크되어있으면 체크 해제
             * else 하나라도 체크가 안되어있으면 모두 체크
             * */
            if (checked_1 && checked_2 && checked_3) {
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
     * phone 번호 유효성 검사 후 인증 요청 logic
     * */
    private fun phone_validation() {
        /**
         * 인증 요청 눌렀을때 logic
         * */
        binding.auth.setOnClickListener {

            val phone = binding.phone.text

            if (Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)) {
                binding.phone.setBackgroundResource(R.drawable.login_activity_white_box)
                Toast.makeText(requireContext(), "휴대번호가 올바르게 입력되었습니다", Toast.LENGTH_SHORT).show()
                authState = true
            } else {
                binding.phone.setBackgroundResource(R.drawable.login_activity_red_box)
                binding.phone.setText("")
                binding.phone.setHint("올바른 휴대번호를 입력해주세요")
                Toast.makeText(requireContext(), "휴대번호가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * phone 번호 유효성 검사 후 인증 요청 logic
     * */
    private fun validation() {
        /**
         * 가입 눌렀을때 logic
         * */
        binding.join.setOnClickListener {

            /**
             * 비밀번호 관련 로직
             * */
            val password = binding.password.text

            if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$", password)) {
                binding.password.setBackgroundResource(R.drawable.login_activity_white_box)
                Toast.makeText(requireContext(), "비밀번호가 올바른 형식입니다", Toast.LENGTH_SHORT).show()

                /**
                 * 비밀번호와 확인 비밀번호 체크
                 * */
                if (password.toString() == binding.passwordRetry.text.toString()) {
                    binding.passwordRetry.setBackgroundResource(R.drawable.login_activity_white_box)
                    Toast.makeText(requireContext(), "비밀번호가 서로 일치합니다", Toast.LENGTH_SHORT).show()
                } else {
                    binding.passwordRetry.setBackgroundResource(R.drawable.login_activity_red_box)
                    binding.passwordRetry.setText("")
                    binding.passwordRetry.setHint("비밀번호를 다시 입력해주세요")
                    Toast.makeText(requireContext(), "비밀번호가 서로 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.password.setBackgroundResource(R.drawable.login_activity_red_box)
                binding.password.setText("")
                Toast.makeText(requireContext(), "비밀번호가 잘못된 형식입니다", Toast.LENGTH_SHORT).show()
            }


            /**
             * 인증이 된 상태인지 확인해서 인증 요구
             * */
            if (authState == true) {
                binding.phone.setBackgroundResource(R.drawable.login_activity_white_box)
            } else {
                binding.phone.setBackgroundResource(R.drawable.login_activity_red_box)
                binding.phone.setText("")
                binding.phone.setHint("휴대번호를 입력해주세요")
                Toast.makeText(requireContext(), "휴대번호가 인증되지 않았습니다", Toast.LENGTH_SHORT).show()
            }

            /**
             * 필수 약관에 동의한 상태인지 확인
             * */
            if (!(binding.checkbox1.isChecked && binding.checkbox2.isChecked)) {
                Toast.makeText(requireContext(), "인증약관에 체크해주세요", Toast.LENGTH_SHORT).show()
            }
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