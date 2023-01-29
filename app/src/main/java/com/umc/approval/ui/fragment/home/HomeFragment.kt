package com.umc.approval.ui.fragment.home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityTok
import androidx.viewpager2.widget.ViewPager2
import com.umc.approval.R
import com.umc.approval.databinding.FragmentHomeBinding
import com.umc.approval.ui.activity.InterestingDepartmentActivity
import com.umc.approval.ui.activity.LoginActivity
import com.umc.approval.ui.activity.SearchActivity
import com.umc.approval.ui.adapter.approval_fragment.CategoryRVAdapter
import com.umc.approval.ui.adapter.home_fragment.ApprovalPaperRVAdapter
import com.umc.approval.ui.adapter.home_fragment.ApprovalReportRVAdapter
import com.umc.approval.ui.adapter.home_fragment.BannerVPAdapter
import com.umc.approval.ui.adapter.home_fragment.PopularPostRVAdapter
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel
import com.umc.approval.ui.viewmodel.community.CommunityReportViewModel
import com.umc.approval.ui.viewmodel.community.CommunityTokViewModel
import com.umc.approval.ui.viewmodel.login.LoginFragmentViewModel
import com.umc.approval.util.InterestingCategory
import me.relex.circleindicator.CircleIndicator3

/**
 * Home View
 */
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    /**login view model*/
    private val viewModel by viewModels<LoginFragmentViewModel>()

    /**Community view model*/
    private val reportViewModel by viewModels<CommunityReportViewModel>()

    /**Community view model*/
    private val tokViewModel by viewModels<CommunityTokViewModel>()

    /**Approval view model*/
    private val approvalViewModel by viewModels<ApprovalViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //엑세스 토큰 체크
        access_token_check()

        //다른 뷰로 이동하는 로직
        move_to_other_view()

        //전체 서류 가지고오는 로직
        approvalViewModel.get_all_documents()

        //관신 서류 가지고오는 로직
        approvalViewModel.get_all_documents()

        //tok 서류 가지고오는 로직
        tokViewModel.get_all_toks(0)

        //report 서류 가지고오는 로직
        reportViewModel.get_all_reports(0)

        //live data
        live_data()

        return binding.root
    }

    /**다른 뷰로 이동하는 로직*/
    private fun move_to_other_view() {
        /**Login Activity로 이동*/
        binding.mypageButton.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        /**Search Activity로 이동*/
        binding.searchButton.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심부서 추가 버튼 클릭")
        }

        binding.myInterestingPaperViewAllButton.setOnClickListener {
            Log.d("로그", "내 관심부서 서류 전체 보기 클릭")
        }

        binding.reviewApprovalPaperViewAllButton.setOnClickListener {
            Log.d("로그", "결재서류 검토하기 전체 보기 클릭")
        }


        binding.popularPostViewAllButton.setOnClickListener {
            Log.d("로그", "인기 게시글 전체 보기 클릭")
        }
        binding.approvalReportViewAllButton.setOnClickListener {
            Log.d("로그", "결재 보고서 전체 보기 클릭")
        }

        setInterestingCategoryList()
        setBannerImage()

        binding.cgApprovalPaperSort.setOnCheckedStateChangeListener { _, checkedIds ->
            Log.d("로그", "결재서류 정렬 방식 선택, $checkedIds")
        }
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        //view 초기화
        binding.notLoginStatus.isVisible = true
        binding.loginStatus.isVisible = false

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()
    }

    /**access token 변화를 fragment에서 체크하는 함수*/
    private fun access_token_check() {
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (it != "") {
                binding.notLoginStatus.isVisible = false
                binding.loginStatus.isVisible = true
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

    // 아이템 간 간격 조절 기능
    inner class HorizontalSpaceItemDecoration(private val width: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = width
        }
    }

    /**live data*/
    private fun live_data() {

        //관심부서
        approvalViewModel.approval_interest_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperRVAdapter(it)
            val spaceDecoration = HorizontalSpaceItemDecoration(40)
            binding.rvMyInterestingPaper.addItemDecoration(spaceDecoration)
            binding.rvMyInterestingPaper.adapter = dataRVAdapter
            binding.rvMyInterestingPaper.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalPaperRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    Log.d("로그", "결재 서류 클릭, pos: $pos")
                }
            })
        }

        //전체부서
        approvalViewModel.approval_all_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperRVAdapter(it)
            val spaceDecoration = HorizontalSpaceItemDecoration(40)
            binding.rvApprovalPaper.addItemDecoration(spaceDecoration)
            binding.rvApprovalPaper.adapter = dataRVAdapter
            binding.rvApprovalPaper.layoutManager = GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalPaperRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    Log.d("로그", "결재 서류 클릭, pos: $pos")
                }
            })
        }

        //tok
        tokViewModel.tok_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = PopularPostRVAdapter(it)
            val spaceDecoration = HorizontalSpaceItemDecoration(40)
            binding.rvPopularPost.addItemDecoration(spaceDecoration)
            binding.rvPopularPost.adapter = dataRVAdapter
            binding.rvPopularPost.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: PopularPostRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: CommunityTok, pos: Int) {
                    Log.d("로그", "인기 게시글 클릭, pos: $pos")
                }
            })
        }

        //report
        reportViewModel.report_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalReportRVAdapter(it)
            val spaceDecoration = HorizontalSpaceItemDecoration(40)
            binding.rvApprovalReport.addItemDecoration(spaceDecoration)
            binding.rvApprovalReport.adapter = dataRVAdapter
            binding.rvApprovalReport.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalReportRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: CommunityReport, pos: Int) {
                    Log.d("로그", "결재 보고서 클릭, pos: $pos")
                }
            })
        }
    }

    private fun setInterestingCategoryList() {
        val interestingCategory: ArrayList<InterestingCategory> = arrayListOf()  // 샘플 데이터

        interestingCategory.apply{
            add(InterestingCategory("관심 부서 전체", true))
            add(InterestingCategory("디지털 기기", false))
            add(InterestingCategory("생활가전", false))
            add(InterestingCategory("생활용품", false))
            add(InterestingCategory("미용", false))
        }

        val categoryRVAdapter = CategoryRVAdapter(interestingCategory)
        val spaceDecoration = HorizontalSpaceItemDecoration(25)
        binding.rvCategory.addItemDecoration(spaceDecoration)
        binding.rvCategory.adapter = categoryRVAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심 부서 추가 버튼 클릭")
            val intent = Intent(requireContext(), InterestingDepartmentActivity::class.java)
            startActivity(intent)
        }

        // 클릭 이벤트 처리
        categoryRVAdapter.setOnItemClickListener(object: CategoryRVAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: InterestingCategory, pos: Int) {
                Log.d("로그", "카테고리 선택, pos: $pos, data: $data")

                // API 호출하여 InterestingCategory 갱신
            }
        })
    }

    /**
     * 배너 이미지 어댑터 설정
     */
    private fun setBannerImage() {
        val photoUrlList = listOf(R.drawable.home_fragment_banner, R.drawable.home_fragment_banner, R.drawable.home_fragment_banner, R.drawable.home_fragment_banner)

        // 뷰페이저에 어댑터 ㅇ녀결
        val photoVPAdatper = BannerVPAdapter(photoUrlList)
        binding.vpHomeBanner.adapter = photoVPAdatper
        binding.vpHomeBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 인디케이터 생성 미 적용
        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(binding.vpHomeBanner)
    }
}