package com.umc.approval.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.umc.approval.BuildConfig
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

        //Kakao SDK를 사용하기 위해선 Native App Key로 초기화
        KakaoSdk.init(requireContext(), BuildConfig.kakao_key)

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                Log.d("KAKAOUSER", user.toString())
                Log.d("KAKAOPROPERTY", user.properties.toString())
                Log.d("KAKAOUSERTOKEN", user.groupUserToken.toString())
                Log.d("KAKAOID", user.id.toString())
                Log.d("KAKAOCONNECTAT", user.connectedAt.toString())
                Log.d("KAKAOKAKAOACCOUNT", user.kakaoAccount.toString())
                Log.d("KAKAOSIGNEDUP", user.hasSignedUp.toString())
            }
        }

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
            }
            else if (tokenInfo != null) {

                Log.d("KAKAOCLIENTAPI", UserApiClient.instance.toString())
                Log.d("KAKAOTOKEN", tokenInfo.toString())
                Log.d("KAKAOTOKENID", tokenInfo.id.toString())
                Log.d("KAKAOTOKENAPPID", tokenInfo.appId.toString())
                Log.d("KAKAOEXPIRES", tokenInfo.expiresIn.toString())
            }
        }

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

                Log.d("KAKAO_IDTOKEN", token.idToken.toString())
                Log.d("KAKAO_REFRESH", token.refreshToken.toString())
                Log.d("KAKAO_ACCESS", token.accessToken.toString())
                Log.d("KAKAO_SCOPE", token.scopes.toString())

                Toast.makeText(requireContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        //로그인 버튼 입력
        binding.kakaoLogin.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())){
                UserApiClient.instance.loginWithKakaoTalk(context = requireContext(), callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(context = requireContext(), callback = callback)
            }
        }

        return view
    }

    /**
     * 구글 로그인 로직
     * */
    private fun google_login() {

        //Google 로그인을 구성하여 사용자 ID와 기본 프로필 정보를 요청하기 위한 객체 생성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestIdToken(BuildConfig.google_client_id)
            .requestServerAuthCode(BuildConfig.google_client_id)
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
            val signIntent: Intent = mGoogleSignInClient.getSignInIntent()
            GoogleSignResultLauncher.launch(signIntent)
        }
    }


    /**
     * 카카오 로그인 로직
     * */
    private fun kakao_login() {
    }

    /**
     * 로그인 상태 확인 메소드
     * */
    override fun onStart() {
        super.onStart()

        /**
         * 구글 로그인 상태 확인
         * */
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account == null) {
            Log.d("INFO", "로그인 안되있음")
        } else {
            Log.d("INFO", "로그인 완료된 상태")
            Log.d("social_google", account.idToken.toString())
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
            binding.emailValid.isVisible = false
            binding.email.text.clear()
        }
    }

    /**
     * 로그인한 사용자 정보가지고 오기
     * GoogleSignInAccount 객체에는 사용자의 이름과 같이 로그인한 사용자에 대한 정보가 포함됨
     * */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val googleIdToken = account?.idToken.toString()
            val authCode = account?.serverAuthCode.toString()

            Log.d("social_google", email)
            Log.d("social_google", googleIdToken)
            Log.d("social_google", authCode)
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