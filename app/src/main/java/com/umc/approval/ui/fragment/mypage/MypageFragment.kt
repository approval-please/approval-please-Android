package com.umc.approval.ui.fragment.mypage
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.marginStart
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.check.collie.OtherpageFragment
import com.umc.approval.databinding.FragmentMypageBinding
import com.umc.approval.ui.activity.ProfileChangeActivity
import com.umc.approval.ui.fragment.mypage.follow.FollowFragment

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

    /*포인트 프로그레스 바 작동 확인을 위한 임시 데이터*/
    var userpoint = 250.0f
    var rankpoint = 1000.0f
    var progress = userpoint / rankpoint * 100.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        /**mypage로 이동*/
        binding.profileFix.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileChangeActivity::class.java))
        }

        /* 프로필 링크 공유 Dialog 하단에 발생 */
        binding.mypageShare.setOnClickListener {
            val shareDialog = ProfileShareDialog()
            shareDialog.show(requireActivity().supportFragmentManager, shareDialog.tag)
        }

        /* 포인트 데이터 프로그레스 바에 반영 */
        binding.pointNum1.text = userpoint.toInt().toString()
        binding.pointNum2.text = " / " + rankpoint.toInt().toString()
        binding.mypageProgressbar.progress = progress.toInt()

        return view
    }

    override fun onStart() {
        super.onStart()

        tab1 = MypageDocumentFragment()
        tab2 = MypageCommunityFragment()
        tab3 = MypageCommentFragment()
        tab4 = MypageScrapFragment()
        tab5 = MypageRecordFragment()
        /* 첫 번째 탭 선택 후 font bold 처리, 해당 Fragment 표시 */
        val viewGroup = binding.mypageTabLayout.getChildAt(0) as ViewGroup
        val viewGroupTab = viewGroup.getChildAt(0) as ViewGroup
        viewGroupTab.forEachIndexed{ index, view ->
            val tabViewChild = viewGroupTab.getChildAt(index)
            if (tabViewChild is TextView){
                tabViewChild.typeface = ResourcesCompat.getFont(requireContext(), R.font.bold)
            }
        }
        replaceView((tab1))
        binding.mypageTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                /* 선택된 탭의 font bold로 처리 */
                val viewGroupTab = viewGroup.getChildAt(tab?.position!!.toInt()) as ViewGroup
                viewGroupTab.forEachIndexed{ index, view ->
                    val tabViewChild = viewGroupTab.getChildAt(index)
                    if (tabViewChild is TextView){
                        tabViewChild.typeface = ResourcesCompat.getFont(context!!, R.font.bold)
                    }
                }
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
                /* 미선택된 탭의 font 다시 medium으로 돌아가도록 처리 */
                val viewGroupTab = viewGroup.getChildAt(tab?.position!!.toInt()) as ViewGroup
                viewGroupTab.forEachIndexed{ index, view ->
                    val tabViewChild = viewGroupTab.getChildAt(index)
                    if (tabViewChild is TextView){
                        tabViewChild.typeface = ResourcesCompat.getFont(context!!, R.font.medium)
                    }
                }
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
            Navigation.findNavController(binding.root).navigate(R.id.action_fragment_mypage_to_settingFragment)
        }
        binding.mypageProgressbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.mypageProgressbar.progress = progress.toInt()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        binding.pointNum2.setOnClickListener {
            val otherpageFragment = OtherpageFragment()
            otherpageFragment.let{
                activity?.supportFragmentManager?.beginTransaction()!!.add(R.id.main_layout, it).commit()
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
    private fun replaceView(tab: Fragment) {
        var selectedFragment: Fragment? = null
        selectedFragment = tab
        selectedFragment.let {
            activity?.supportFragmentManager?.beginTransaction()!!
                .replace(binding.mypageTabArea.id, it).commit()
        }
    }
}