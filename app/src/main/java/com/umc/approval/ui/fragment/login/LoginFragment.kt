package com.umc.approval.ui.fragment.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

        return view
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
            val dialog_cancel = alertDialog.findViewById<ImageView>(R.id.back)
            val keep_going = alertDialog.findViewById<ImageView>(R.id.back_fragment)

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

                /**
                 * 로그인 성공시 서버로 엑세스토큰 발급 요청
                 * */
                viewModel.login(token.accessToken.toString(), "kakao")
            }
        }

        //로그인 버튼 입력시 dialog 보여줌 그 후에 로그인 진행
        binding.kakaoLogin.setOnClickListener {

            val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.social_kakao_login_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialog)

            val alertDialog = builder.show()

            //dialog의 view Component 접근
            val dialog_cancel = alertDialog.findViewById<ImageView>(R.id.back)
            val keep_going = alertDialog.findViewById<ImageView>(R.id.back_fragment)

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
     * 시작시 로그인 상태 확인 메소드
     * */
    override fun onStart() {
        super.onStart()

        /**
         * AccessToken 확인해서 로그인 상태인지 아닌지 확인
         * */
        viewModel.checkAccessToken()
    }

    /**access token 변화를 fragment에서 체크하는 함수*/
    private fun access_token_check() {
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (it != "") {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

                viewModel.deleteAccessToken()
            }
        }
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

            /**
             * 로그인 성공시 서버로부터 엑세스 토큰 발급
             * */
            viewModel.login(googleIdToken, "google")
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