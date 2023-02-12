package com.umc.approval.ui.fragment.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.approval.API.GOOGLE_CLIENT_ID
import com.umc.approval.API.KAKAO_KEY
import com.umc.approval.R
import com.umc.approval.databinding.FragmentLoginBinding
import com.umc.approval.ui.activity.MainActivity
import com.umc.approval.ui.viewmodel.login.LoginFragmentViewModel
import java.util.regex.Pattern

/**
 * Login View
 * In here, User can social login or basic login to account
 * */
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginFragmentViewModel>()

    private lateinit var GoogleSignResultLauncher: ActivityResultLauncher<Intent>

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

        //google 로그인
        google_login()

        //kakao 로그인
        kakao_login()

        //엑세스 토큰
        access_token_check()

        //카카오 로그인 시
        viewModel.social_status.observe(viewLifecycleOwner) {
            val status = viewModel.social_status.value!!.status

            //최초 로그인 시
            if (status == 0) {
                val to_social_join = LoginFragmentDirections.actionLoginFragmentToSocialJoinFragment(
                    viewModel.kakao_email.value.toString(), viewModel.social_id.value.toString())
                Navigation.findNavController(binding.root).navigate(to_social_join)
            } else if (status == 2) {
                Toast.makeText(requireContext(), "계정이 존재합니다", Toast.LENGTH_SHORT).show()
            } else if (status == 1) {
                viewModel.setAccessToken("Bearer " + viewModel.social_status.value!!.accessToken.toString())
                Handler(Looper.myLooper()!!).postDelayed({
                    requireActivity().finish()
                }, 300)
            }
        }

        return view
    }

    /**시작시 로그인 상태 확인 메소드*/
    override fun onStart() {
        super.onStart()
        viewModel.checkAccessToken()
    }

    /**엑세스 토큰이 존재하는 경우 삭제*/
    private fun access_token_check() {
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (it == true) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    /**
     * main activity로 이동
     * */
    private fun back_to_main_activity() {
        binding.backToMainActivity.setOnClickListener {
            requireActivity().finish()
        }
    }

    /**
     * 구글 로그인 로직
     * */
    private fun google_login() {

        //Google 로그인을 구성하여 사용자 ID와 기본 프로필 정보를 요청하기 위한 객체 생성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestServerAuthCode(GOOGLE_CLIENT_ID)
            .requestProfile()
            .requestEmail()
            .build()

        //GoogleSignInClient 객체를 생성
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //사용자가 로그인하면 활동의 registerForActivityResult를 통해 사용자의 GoogleSignInAccount 객체를 가져올 수 있음
        GoogleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }

        //인텐트를 시작하면 사용자에게 로그인할 Google 계정을 선택하라는 메시지가 표시됨
        binding.googleLogin.setOnClickListener {

            val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.social_google_login_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialog)

            val alertDialog = builder.show()

            //dialog의 view Component 접근
            val dialog_cancel = alertDialog.findViewById<TextView>(R.id.back)
            val keep_going = alertDialog.findViewById<TextView>(R.id.back_fragment)

            dialog_cancel.setOnClickListener {
                alertDialog.cancel()
            }

            keep_going.setOnClickListener {
                val signIntent: Intent = mGoogleSignInClient.getSignInIntent()
                GoogleSignResultLauncher.launch(signIntent)
                alertDialog.cancel()
            }
        }
    }

    /**
     * 카카오 로그인 로직
     * */
    private fun kakao_login() {
        //Kakao SDK를 사용하기 위해선 Native App Key로 초기화
        KakaoSdk.init(requireContext(), KAKAO_KEY)

        /**
         *  로그인 실패시 callback
         * */
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(requireContext(), "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(requireContext(), "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(requireContext(), "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(requireContext(), "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(requireContext(), "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(requireContext(), "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(requireContext(), "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(requireContext(), "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(requireContext(), "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {

                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                    }
                    else if (user != null) {
                        //로그인 진행
                        viewModel.social_login(token.accessToken.toString(),
                            user.kakaoAccount!!.email.toString(), user.id.toString())
                    }
                }
            }
        }

        //로그인 버튼 입력시 dialog 보여줌 그 후에 로그인 진행
        binding.kakaoLogin.setOnClickListener {

            val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.social_kakao_login_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialog)

            val alertDialog = builder.show()

            //dialog의 view Component 접근
            val dialog_cancel = alertDialog.findViewById<TextView>(R.id.back)
            val keep_going = alertDialog.findViewById<TextView>(R.id.back_fragment)

            dialog_cancel.setOnClickListener {
                alertDialog.cancel()
            }

            keep_going.setOnClickListener {
                if(UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())){
                    UserApiClient.instance.loginWithKakaoTalk(context = requireContext(), callback = callback)
                }else{
                    UserApiClient.instance.loginWithKakaoAccount(context = requireContext(), callback = callback)
                }
                alertDialog.cancel()
            }
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

                viewModel.emailCheck(binding.email.text.toString())

                //check가 성공적으로 진행되었을때
                viewModel.email_check.observe(viewLifecycleOwner) {

                    val to_password = LoginFragmentDirections.actionLoginFragmentToPasswordFragment(binding.email.text.toString())
                    val to_join = LoginFragmentDirections.actionLoginFragmentToJoinFragment(binding.email.text.toString())

                    if (viewModel.email_check.value!!.status == 1) { //일반 회원인 경우
                        Navigation.findNavController(binding.root).navigate(to_password)
                    } else if (viewModel.email_check.value!!.status == 0) { //회원이 아닌 경우
                        Navigation.findNavController(binding.root).navigate(to_join)
                    } else if (viewModel.email_check.value!!.status == 2) { //sns 회원인 경우

                        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.join_fragment_dialog, null)
                        val builder = AlertDialog.Builder(requireContext()).setView(dialog)

                        val alertDialog = builder.show()

                        //dialog의 view Component 접근
                        val dialog_cancel = alertDialog.findViewById<TextView>(R.id.back)
                        val keep_going = alertDialog.findViewById<TextView>(R.id.back_fragment)
                        val email_name = alertDialog.findViewById<TextView>(R.id.email_name)

                        email_name.setText(viewModel.email_check.value!!.email)

                        dialog_cancel.setOnClickListener {
                            alertDialog.cancel()
                        }

                        keep_going.setOnClickListener {
                            alertDialog.cancel()
                        }
                    }
                }
            } else {
                binding.email.setBackgroundResource(R.drawable.login_activity_red_box)
                binding.validFail.isVisible = true
                binding.textRemove.isVisible = false
                binding.emailValid.isVisible = true
            }
        }
    }

    /**
     * x버튼 누르면 입력중인 내용 모두 삭제
     * */
    private fun init_email() {
        binding.textRemove.setOnClickListener {
            binding.email.text.clear()
        }
    }

    /**
     * GoogleSignInAccount 객체에는 사용자의 이름과 같이 로그인한 사용자에 대한 정보가 포함됨
     * */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val googleIdToken = account?.idToken.toString()

        } catch (e: ApiException){
            Log.d("INFO","signInResult:failed Code = " + e.statusCode)
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