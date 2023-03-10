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
import com.umc.approval.util.BlackToast
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

        //main activity??? ???????????? logic
        back_to_main_activity()

        //email text ????????? logic
        init_email()

        //email ?????? logic
        validate_email()

        //google ?????????
        google_login()

        //kakao ?????????
        kakao_login()

        //????????? ??????
        access_token_check()

        //????????? ????????? ???
        viewModel.social_status.observe(viewLifecycleOwner) {
            val status = viewModel.social_status.value!!.status

            //?????? ????????? ???
            if (status == 0) {
                val to_social_join = LoginFragmentDirections.actionLoginFragmentToSocialJoinFragment(
                    viewModel.kakao_email.value.toString(), viewModel.social_id.value.toString())
                Navigation.findNavController(binding.root).navigate(to_social_join)
            } else if (status == 2) {
                BlackToast.createToast(requireContext(), "????????? ??????????????? ?????????????????????.").show()
            } else if (status == 1) {
                viewModel.setAccessToken("Bearer " + viewModel.social_status.value!!.accessToken.toString())
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }, 300)
            }
        }

        return view
    }

    /**????????? ????????? ?????? ?????? ?????????*/
    override fun onStart() {
        super.onStart()
        viewModel.checkAccessToken()
    }

    /**????????? ????????? ???????????? ?????? ??????*/
    private fun access_token_check() {
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (it == true) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    /**
     * main activity??? ??????
     * */
    private fun back_to_main_activity() {
        binding.backToMainActivity.setOnClickListener {
            requireActivity().finish()
        }
    }

    /**
     * ?????? ????????? ??????
     * */
    private fun google_login() {

        //Google ???????????? ???????????? ????????? ID??? ?????? ????????? ????????? ???????????? ?????? ?????? ??????
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestServerAuthCode(GOOGLE_CLIENT_ID)
            .requestProfile()
            .requestEmail()
            .build()

        //GoogleSignInClient ????????? ??????
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //???????????? ??????????????? ????????? registerForActivityResult??? ?????? ???????????? GoogleSignInAccount ????????? ????????? ??? ??????
        GoogleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }

        //???????????? ???????????? ??????????????? ???????????? Google ????????? ??????????????? ???????????? ?????????
        binding.googleLogin.setOnClickListener {

            val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.social_google_login_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialog)

            val alertDialog = builder.show()

            //dialog??? view Component ??????
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
     * ????????? ????????? ??????
     * */
    private fun kakao_login() {
        //Kakao SDK??? ???????????? ????????? Native App Key??? ?????????
        KakaoSdk.init(requireContext(), KAKAO_KEY)

        /**
         *  ????????? ????????? callback
         * */
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(requireContext(), "????????? ?????? ???(?????? ??????)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(requireContext(), "???????????? ?????? ???", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(requireContext(), "?????? ????????? ???????????? ?????? ????????? ??? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(requireContext(), "?????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(requireContext(), "???????????? ?????? scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(requireContext(), "????????? ???????????? ??????(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(requireContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(requireContext(), "?????? ?????? ????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(requireContext(), "?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {

                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                    }
                    else if (user != null) {
                        //????????? ??????
                        viewModel.social_login(token.accessToken.toString(),
                            user.kakaoAccount!!.email.toString(), user.id.toString())
                    }
                }
            }
        }

        //????????? ?????? ????????? dialog ????????? ??? ?????? ????????? ??????
        binding.kakaoLogin.setOnClickListener {

            val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.social_kakao_login_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialog)

            val alertDialog = builder.show()

            //dialog??? view Component ??????
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
     * email ????????? ?????? ??? ????????? ???????????? ?????? password fragment??? ??????
     * */
    private fun validate_email() {
        binding.emailLoginButton.setOnClickListener {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS

            val email = binding.email.text

            if (pattern.matcher(email).matches()) {
                binding.emailValid.isVisible = false

                viewModel.emailCheck(binding.email.text.toString())

                //check??? ??????????????? ??????????????????
                viewModel.email_check.observe(viewLifecycleOwner) {

                    if (viewModel.email_check.value!!.status == 1) { //?????? ????????? ??????
                        val to_password = LoginFragmentDirections.actionLoginFragmentToPasswordFragment(binding.email.text.toString())
                        Navigation.findNavController(binding.root).navigate(to_password)
                    } else if (viewModel.email_check.value!!.status == 0) { //????????? ?????? ??????
                        val to_join = LoginFragmentDirections.actionLoginFragmentToJoinFragment(binding.email.text.toString())
                        Navigation.findNavController(binding.root).navigate(to_join)
                    } else if (viewModel.email_check.value!!.status == 2) { //sns ????????? ??????

                        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.join_fragment_dialog, null)
                        val builder = AlertDialog.Builder(requireContext()).setView(dialog)

                        val alertDialog = builder.show()

                        //dialog??? view Component ??????
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
     * x?????? ????????? ???????????? ?????? ?????? ??????
     * */
    private fun init_email() {
        binding.textRemove.setOnClickListener {
            binding.email.text.clear()
        }
    }

    /**
     * GoogleSignInAccount ???????????? ???????????? ????????? ?????? ???????????? ???????????? ?????? ????????? ?????????
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
     * viewBinding??? ????????? ?????? ?????? ?????? null ?????? ??????
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}