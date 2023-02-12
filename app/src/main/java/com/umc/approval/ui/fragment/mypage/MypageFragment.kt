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
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.databinding.FragmentMypageBinding
import com.umc.approval.ui.activity.LoginActivity
import com.umc.approval.ui.activity.ProfileChangeActivity
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel
import com.umc.approval.util.Utils

/**
 * MyPage View
 * */
class MypageFragment : Fragment() {

    /**init binding*/
    private var _binding : FragmentMypageBinding? = null
    private val binding get() = _binding!!

    /**tab layout*/
    lateinit var tab1: MypageDocumentFragment
    lateinit var tab2: MypageCommunityFragment
    lateinit var tab3: MypageCommentFragment
    lateinit var tab4: MypageScrapFragment
    lateinit var tab5: MypageRecordFragment

    /**mypage view model*/
    private val viewModel by viewModels<MypageViewModel>()

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

        //다른 뷰로 이동
        move_to_other_view()

        //tab layout 초기화
        init_tab_layout()

        //서버로부터 데이터 받아오기
        viewModel.my_profile()

        //서버로부터 데이터를 받아온 것을 관찰했을 경우
        profile_live_data()

        /* 프로필 링크 공유 Dialog 하단에 발생 */
        binding.mypageShare.setOnClickListener {

            var sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.setType("text/html")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "profilelink")
            startActivity(Intent.createChooser(sharingIntent, "sharing text"))
            /*
            val shareDialog = ProfileShareDialog()
            shareDialog.show(requireActivity().supportFragmentManager, shareDialog.tag)
            */
        }

        /* 포인트 데이터 프로그레스 바에 반영 */
        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkAccessToken()
    }

    override fun onResume() {
        super.onResume()
        viewModel.my_profile()
    }

    /**profile live data*/
    private fun profile_live_data() {

        //엑세스 토큰 확인하는 라이브 데이터
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (it == false) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }

        viewModel.myInfo.observe(viewLifecycleOwner) {

            //follower
            binding.followerTextview.setText("팔로워 " + it.follows.toString())
            //following
            binding.followingTextview.setText("팔로잉 " + it.followings.toString())
            //nickname
            binding.nicknameTextview.setText(it.nickname)
            //introduce
            binding.profileMsgTextview.setText(it.introduction)
            //point
            userpoint = it.promotionPoint.toFloat()
            rankpoint = setRankPoint(it.level).toFloat()

            progress = userpoint / rankpoint * 100.0f
            binding.pointNum1.text = userpoint.toInt().toString()
            binding.pointNum2.text = " / " + rankpoint.toInt().toString()
            binding.mypageProgressbar.progress = progress.toInt()
            //rank
            var rank : String? = setRank(it.level)
            binding.rank.text = rank
            //profile image
            if (viewModel.myInfo.value!!.profileImage != null) {
                binding.profileImage.load(it.profileImage)
            } else {
                binding.profileImage.load(Utils.levelImage[it.level])
            }
            else {
                when (it.level) {
                    0 -> binding.profileImage.load(R.drawable.profile_img_sawon)
                    1 -> binding.profileImage.load(R.drawable.profile_img_juim)
                    2 -> binding.profileImage.load(R.drawable.profile_img_daeli)
                    3 -> binding.profileImage.load(R.drawable.profile_img_gwajang)
                    4 -> binding.profileImage.load(R.drawable.profile_img_chajang)
                    5 -> binding.profileImage.load(R.drawable.profile_img_bujang)
                }
            }
        }
    }

    /**Tab layout 초기화*/
    private fun init_tab_layout() {
        tab1 = MypageDocumentFragment()
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
                        tab1 = MypageDocumentFragment()
                        replaceView(tab1)
                    }
                    1 -> {
                        tab2 = MypageCommunityFragment()
                        replaceView(tab2)
                    }
                    2->{
                        tab3 = MypageCommentFragment()
                        replaceView(tab3)
                    }
                    3->{
                        tab4 = MypageScrapFragment()
                        replaceView(tab4)
                    }
                    4 -> {
                        tab5 = MypageRecordFragment()
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
        binding.mypageProgressbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.mypageProgressbar.progress = progress.toInt()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
//        binding.pointNum2.setOnClickListener {
//            val otherpageFragment = OtherpageFragment()
//            otherpageFragment.let{
//                activity?.supportFragmentManager?.beginTransaction()!!.add(R.id.main_layout, it).commit()
//            }
//        }
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
        selectedFragment = tab
        selectedFragment.let {
            activity?.supportFragmentManager?.beginTransaction()!!
                .replace(binding.mypageTabArea.id, it).commit()
        }
    }

    /* 문자열로 직급 반환하는 함수 */
    private fun setRank(rankInt : Int) : String?{
        var rank : String? = null
        when(rankInt){
            0->{ rank = "사원" }
            1->{ rank = "주임" }
            2->{ rank = "대리" }
            3->{ rank = "과장" }
            4->{ rank = "차장" }
            5->{ rank = "부장" }
        }
        return rank
    }

    /* 직급 별로 기준 승진 포인트 반환하는 함수 */
    private fun setRankPoint(rankInt : Int?) : Int{
        var point : Int = 0
        when(rankInt){
            0->{ point = 7000 }
            1->{ point = 21000 }
            2->{ point = 33000 }
            3->{ point = 50000 }
            4->{ point = 71000 }
            5->{ point = 71000 }
        }
        return point
    }
}