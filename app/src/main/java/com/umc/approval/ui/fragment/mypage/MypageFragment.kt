package com.umc.approval.ui.fragment.mypage
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import coil.load
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.databinding.FragmentMypageBinding
import com.umc.approval.ui.activity.ProfileChangeActivity
import com.umc.approval.ui.fragment.approval.ApprovalAllCategoryViewFragment
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel

/**
 * MyPage View
 * */
class MypageFragment : Fragment() {

    /**init binding*/
    private var _binding : FragmentMypageBinding? = null
    private val binding get() = _binding!!

    /**tab layout*/
    lateinit var tab1: ApprovalAllCategoryViewFragment
    lateinit var tab2: MypageCommunityFragment
    lateinit var tab3: MypageCommentFragment
    lateinit var tab4: MypageScrapFragment
    lateinit var tab5: MypageRecordFragment

    /**mypage view model*/
    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        //다른 뷰로 이동
        move_to_other_view()

        //tab layout 초기화
        init_tab_layout()

        //init_viewmodel
//        viewModel.init_test_data()

        //서버로부터 데이터 받아오기
        viewModel.my_profile()

        //서버로부터 데이터를 받아온 것을 관찰했을 경우
        profile_live_data()

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "테스트")
        viewModel.my_profile()
    }

    /**profile live data*/
    private fun profile_live_data() {
        viewModel.myInfo.observe(viewLifecycleOwner) {

            //follower
            binding.followerNumTextview.setText(viewModel.myInfo.value!!.follows.toString())
            //following
            binding.followingNumTextview.setText(viewModel.myInfo.value!!.followings.toString())
            //nickname
            binding.nicknameTextview.setText(viewModel.myInfo.value!!.nickname)
            //introduce
            binding.profileMsgTextview.setText(viewModel.myInfo.value!!.introduction)
            //point
            //rank

            //profile image
            if (!viewModel.myInfo.value!!.profileImage.equals(null)) {
                binding.profileImage.load(viewModel.myInfo.value!!.profileImage)
            }
        }
    }

    /**Tab layout 초기화*/
    private fun init_tab_layout() {
        tab1 = ApprovalAllCategoryViewFragment()
        tab2 = MypageCommunityFragment()
        tab3 = MypageCommentFragment()
        tab4 = MypageScrapFragment()
        tab5 = MypageRecordFragment()
        replaceView((tab1))
        binding.mypageTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceView(tab1)
                    }
                    1 -> {
                        replaceView(tab2)
                    }
                    2 -> {
                        replaceView(tab3)
                    }
                    3 -> {
                        replaceView(tab4)
                    }
                    4 -> {
                        replaceView(tab5)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    /**다른 UI로 이동하는 함수*/
    private fun move_to_other_view() {
        //follow 목록으로 이동
        binding.followerTextview.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_fragment_mypage_to_followFragment)
        }
        //follow 목록으로 이동
        binding.followingTextview.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_fragment_mypage_to_followFragment)
        }
        //setting 으로 이동
        binding.mypageSetting.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_fragment_mypage_to_settingFragment)
        }//profile 수정으로 이동
        binding.profileFix.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileChangeActivity::class.java))
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**탭 클릭시 변경*/
    private fun replaceView(tab: Fragment){
        var selectedFragment = tab
        selectedFragment.let {
            activity?.supportFragmentManager?.beginTransaction()!!
                .replace(binding.mypageTabArea.id, it).commit()
        }
    }
}