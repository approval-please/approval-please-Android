package com.umc.approval.check.collie

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.mypage.FollowDto
import com.umc.approval.databinding.ActivityOtherpageBinding
import com.umc.approval.ui.adapter.follow_fragment.FollowerAdapter
import com.umc.approval.ui.viewmodel.follow.FollowViewModel
import com.umc.approval.ui.viewmodel.otherpage.OtherpageViewModel

class OtherpageActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtherpageBinding

    /* 탭 */
    lateinit var tab1 : OtherpageDocumentFragment
    lateinit var tab2 : OtherpageCommunityFragment

    /* 포인트 프로그레스 바 데이터 */
    var userpoint = 0f
    var rankpoint = 0f
    var progress = 0f

    var userId : Int = 0

    /* otherpage viewModel */
    private val viewModel by viewModels<OtherpageViewModel>()

    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherpageBinding.inflate(layoutInflater)
        val view = binding.root
        val intent = intent
        userId = intent.getIntExtra("userId", 0)
        setContentView(view)
    }

    // 시작 시 로그인 상태 검증 및 좋아요 누른 유저 목록 조회
    override fun onStart() {
        super.onStart()
        viewModel.init_other_profile()
        viewModel.other_profile(userId)

        // 탭 초기화
        init_tab()
        move_to_other_tab()
        seekbar_inactive()
        others_profile_live_data()

        //버튼 클릭
        binding.followBtn.setOnClickListener {
            followViewModel.follow(userId)
        }

        /* 프로필 링크 공유 Dialog 하단에 발생 */
        binding.otherpageShare.setOnClickListener {
            var sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.setType("text/html")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "profilelink")
            startActivity(Intent.createChooser(sharingIntent, "sharing text"))
            /*
            val shareDialog = ProfileShareDialog()
            shareDialog.show(requireActivity().supportFragmentManager, shareDialog.tag)
            */
        }

    }

    // 탭 초기화 함수
    private fun init_tab(){
        tab1 = OtherpageDocumentFragment(userId)
        tab2 = OtherpageCommunityFragment(userId)

        /* 첫 번째 탭 선택 후 font bold 처리, 해당 Fragment 표시 */
        val viewGroup = binding.otherpageTabLayout.getChildAt(0) as ViewGroup
        val viewGroupTab = viewGroup.getChildAt(0) as ViewGroup
        viewGroupTab.forEachIndexed { index, view ->
            val tabViewChild = viewGroupTab.getChildAt(index)
            if (tabViewChild is TextView){
                tabViewChild.typeface = ResourcesCompat.getFont(this, R.font.bold)
            }
        }
        replaceView(tab1)
    }

    // 탭 이동 함수
    private fun move_to_other_tab(){
        binding.otherpageTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                /* 선택된 탭의 font bold로 처리 */
                val viewGroup = binding.otherpageTabLayout.getChildAt(0) as ViewGroup
                val viewGroupTab = viewGroup.getChildAt(tab?.position!!.toInt()) as ViewGroup
                viewGroupTab.forEachIndexed { index, view ->
                    val tabViewChild = viewGroupTab.getChildAt(index)
                    if (tabViewChild is TextView){
                        tabViewChild.typeface = ResourcesCompat.getFont(this@OtherpageActivity, R.font.bold)
                    }
                }
                when(tab?.position){
                    0 -> {
                        tab1 = OtherpageDocumentFragment(userId)
                        replaceView(tab1)
                    }
                    1 -> {
                        tab2 = OtherpageCommunityFragment(userId)
                        replaceView(tab2)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                /* 미선택된 탭의 font 다시 medium으로 돌아가도록 처리 */
                val viewGroup = binding.otherpageTabLayout.getChildAt(0) as ViewGroup
                val viewGroupTab = viewGroup.getChildAt(tab?.position!!.toInt()) as ViewGroup
                viewGroupTab.forEachIndexed{ index, view ->
                    val tabViewChild = viewGroupTab.getChildAt(index)
                    if (tabViewChild is TextView){
                        tabViewChild.typeface = ResourcesCompat.getFont(this@OtherpageActivity, R.font.medium)
                    }
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    // 프로그래스 바 터치 막는 함수
    private fun seekbar_inactive(){
        binding.otherpageProgressbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.otherpageProgressbar.progress = progress.toInt()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    // 라이브 데이터 함수
    private fun others_profile_live_data(){

        followViewModel.isFollow.observe(this) {

            when(it.isFollow){
                // 팔로잉 중
                true->{
                    binding.followBtn.background.setTint(Color.parseColor("#BFBFBF"))
                    binding.followBtn.text = "팔로잉"
                }
                // 팔로우 x
                false->{
                    binding.followBtn.background.setTint(Color.parseColor("#6C39FF"))
                    binding.followBtn.text = "팔로우"
                }
            }
        }

        viewModel.othersInfo.observe(this){
            //프로필 이미지
            if (viewModel.othersInfo.value!!.profileImage != null) {
                binding.otherpageProfilePic.load(it.profileImage)
            }
            // 직급
            binding.otherpageRank.text = setRank(it.level)
            // 닉네임
            binding.otherpageNicknameTextview.text = it.nickname
            // 팔로잉
            binding.otherpageFollowingTextview.text = "팔로잉 " + it.followings
            // 팔로워
            binding.otherpageFollowerTextview.text = "팔로워 " + it.follows
            // 프로필 메세지
            binding.profileMsgTextview.text = it.introduction
            // 승진 포인트
            userpoint = it.promotionPoint.toFloat()
            rankpoint = setRankPoint(it.level).toFloat()
            progress = userpoint / rankpoint * 100.0f
            binding.otherpageProgressbar.progress = progress.toInt()
            binding.pointNum1.text = userpoint.toInt().toString()
            binding.pointNum2.text = " / " + rankpoint.toInt().toString()

            followViewModel.setFollow(FollowStateDto(it.isFollow))
        }
    }

    // 탭 변경 함수
    private fun replaceView(tab: Fragment){
        var selectedFragment: Fragment? = null
        selectedFragment = tab
        selectedFragment.let {
            supportFragmentManager?.beginTransaction()!!
                .replace(binding.otherpageTabArea.id, it).commit()
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