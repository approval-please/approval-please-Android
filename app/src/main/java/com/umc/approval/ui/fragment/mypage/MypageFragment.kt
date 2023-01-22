package com.umc.approval.ui.fragment.mypage
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.databinding.FragmentMypageBinding
import com.umc.approval.ui.fragment.mypage.follow.FollowFragment
import com.umc.approval.ui.fragment.mypage.follow.FollowerFragment
import com.umc.approval.ui.fragment.mypage.setting.SettingFragment

/**
 * MyPage View
 * */
class MypageFragment : Fragment() {

    private var _binding : FragmentMypageBinding? = null
    private val binding get() = _binding!!
    lateinit var tab1: MypageDocumentFragment
    lateinit var tab2: MypageCommunityFragment
    lateinit var tab3: MypageCommentFragment
    lateinit var tab4: MypageScrapFragment
    lateinit var tab5: MypageRecordFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        tab1 = MypageDocumentFragment()
        tab2 = MypageCommunityFragment()
        tab3 = MypageCommentFragment()
        tab4 = MypageScrapFragment()
        tab5 = MypageRecordFragment()
        replaceView((tab1))
        binding.mypageTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        replaceView(tab1)
                    }
                    1 -> {
                        replaceView(tab2)
                    }
                    2->{
                        replaceView(tab3)
                    }
                    3->{
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
        binding.followerTextview.setOnClickListener {
            val followFragment = FollowFragment()
            followFragment.let{
                activity?.supportFragmentManager?.beginTransaction()!!
                    .add(R.id.main_layout, it).commit()
            }
        }
        binding.followingTextview.setOnClickListener {
            val followFragment = FollowFragment()
            followFragment.let{
                activity?.supportFragmentManager?.beginTransaction()!!
                    .add(R.id.main_layout, it).commit()
            }
        }
        binding.mypageSetting.setOnClickListener {
            val settingFragment = SettingFragment()
            settingFragment.let{
                activity?.supportFragmentManager?.beginTransaction()!!
                    .add(R.id.main_layout, it).commit()
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

    // 탭 변경 함수
    private fun replaceView(tab: Fragment){
        var selectedFragment: Fragment? = null
        selectedFragment = tab
        selectedFragment.let {
            activity?.supportFragmentManager?.beginTransaction()!!
                .replace(binding.mypageTabArea.id, it).commit()
        }
    }
}