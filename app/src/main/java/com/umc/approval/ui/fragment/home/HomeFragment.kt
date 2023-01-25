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
import com.umc.approval.R
import com.umc.approval.databinding.FragmentHomeBinding
import com.umc.approval.ui.activity.LoginActivity
import com.umc.approval.ui.activity.MainActivity
import com.umc.approval.ui.activity.SearchActivity
import com.umc.approval.ui.adapter.home_fragment.ApprovalPaperRVAdapter
import com.umc.approval.ui.adapter.home_fragment.ApprovalReportRVAdapter
import com.umc.approval.ui.adapter.home_fragment.PopularPostRVAdapter
import com.umc.approval.ui.viewmodel.login.LoginFragmentViewModel
import com.umc.approval.util.ApprovalPaper
import com.umc.approval.util.ApprovalReport
import com.umc.approval.util.Post

/**
 * Home View
 */
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    /**login view model*/
    private val viewModel by viewModels<LoginFragmentViewModel>()

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

        // 각 리사이클러뷰 데이터 & 어댑터 설정
        setInterestingDepartment()  // 관심 부서 서류
        setReviewApprovalPaper()  // 결재서류 검토하기
        setPopularPost()  // 인기 게시글
        setApprovalReport()  // 결재 보고서

        binding.cgMyInterestingCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            Log.d("로그", "관심 부서 선택, $checkedIds")
            // 카테고리 번호 선택, notify
        }

        binding.cgApprovalPaperSort.setOnCheckedStateChangeListener { _, checkedIds ->
            Log.d("로그", "결재서류 정렬 방식 선택, $checkedIds")
        }

        return binding.root
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

    private fun setInterestingDepartment() {
        val approvalPaperList: ArrayList<ApprovalPaper> = arrayListOf()  // 샘플 데이터

        approvalPaperList.apply{
            add(ApprovalPaper(0, "스타벅스 텀블러 1", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", null))
            add(ApprovalPaper(2, "스타벅스 텀블러 2", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 4, 3, 2, "디지털기기", "5시간 전", R.drawable.item_home_image))
            add(ApprovalPaper(1, "스타벅스 텀블러 3", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 1, 10, "디지털기기", "5시간 전", R.drawable.item_home_image))
            add(ApprovalPaper(1, "스타벅스 텀블러 4", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", null))
            add(ApprovalPaper(0, "스타벅스 텀블러 5", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 5, "디지털기기", "5시간 전", null))
            add(ApprovalPaper(1, "스타벅스 텀블러 6", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", R.drawable.item_home_image))
            add(ApprovalPaper(2, "스타벅스 텀블러 7", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", null))
        }

        val dataRVAdapter = ApprovalPaperRVAdapter(approvalPaperList)
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

    private fun setReviewApprovalPaper() {
        val approvalPaperList: ArrayList<ApprovalPaper> = arrayListOf()  // 샘플 데이터

        approvalPaperList.apply{
            add(ApprovalPaper(0, "스타벅스 텀블러 1", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", null))
            add(ApprovalPaper(2, "스타벅스 텀블러 2", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라 테스트테스트테스트테스트테스트", 4, 3, 2, "디지털기기", "5시간 전", R.drawable.item_home_image))
            add(ApprovalPaper(1, "스타벅스 텀블러 3", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 1, 10, "디지털기기", "5시간 전", R.drawable.item_home_image))
            add(ApprovalPaper(1, "스타벅스 텀블러 4", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", null))
            add(ApprovalPaper(0, "스타벅스 텀블러 5", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 5, "디지털기기", "5시간 전", null))
            add(ApprovalPaper(1, "스타벅스 텀블러 6", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", R.drawable.item_home_image))
            add(ApprovalPaper(2, "스타벅스 텀블러 7", "스타벅스 텀블러 골라주세요! 테스트테스트블라블라", 5, 3, 2, "디지털기기", "5시간 전", null))
        }

        val dataRVAdapter = ApprovalPaperRVAdapter(approvalPaperList)
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

    private fun setPopularPost() {
        val postList: ArrayList<Post> = arrayListOf()

        postList.apply {
            add(Post("", "강사원", "부장", 10, "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", 5, 2, "5시간 전"))
            add(Post("", "채사원", "대리", 10, "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요 테스트테스트", 5, 2, "5시간 전"))
            add(Post("", "김사원", "인턴", 10, "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요 테스트테스트 테스트테스트", 5, 2, "5시간 전"))
            add(Post("", "유사원", "사장", 10, "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요 테스트테스트 테스트테스트 테스트테스트", 5, 2, "5시간 전"))
            add(Post("", "박사원", "부장", 10, "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요 테스트테스트", 5, 2, "5시간 전"))
        }

        val dataRVAdapter = PopularPostRVAdapter(postList)
        val spaceDecoration = HorizontalSpaceItemDecoration(40)
        binding.rvPopularPost.addItemDecoration(spaceDecoration)
        binding.rvPopularPost.adapter = dataRVAdapter
        binding.rvPopularPost.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        // 클릭 이벤트 처리
        dataRVAdapter.setOnItemClickListener(object: PopularPostRVAdapter.OnItemClickListner {
            override fun onItemClick(v: View, data: Post, pos: Int) {
                Log.d("로그", "인기 게시글 클릭, pos: $pos")
            }
        })
    }

    private fun setApprovalReport() {
        val approvalReportList: ArrayList<ApprovalReport> = arrayListOf()

        approvalReportList.apply {
            add(ApprovalReport("", "강사원", "부장", "집에 텀블러 다섯개", "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", "", 10, 5, 2, "5시간 전"))
            add(ApprovalReport("", "강사원", "부장", "집에 텀블러 다섯개", "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", "", 10, 5, 2, "5시간 전"))
            add(ApprovalReport("", "강사원", "부장", "집에 텀블러 다섯개", "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", "", 10, 5, 2, "5시간 전"))
            add(ApprovalReport("", "강사원", "부장", "집에 텀블러 다섯개", "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", "", 10, 5, 2, "5시간 전"))
            add(ApprovalReport("", "강사원", "부장", "집에 텀블러 다섯개", "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", "", 10, 5, 2, "5시간 전"))
            add(ApprovalReport("", "강사원", "부장", "집에 텀블러 다섯개", "집에 텀블러 다섯개 있는데\n이 사이즈는 없어서 고민돼요", "", 10, 5, 2, "5시간 전"))
        }

        val dataRVAdapter = ApprovalReportRVAdapter(approvalReportList)
        val spaceDecoration = HorizontalSpaceItemDecoration(40)
        binding.rvApprovalReport.addItemDecoration(spaceDecoration)
        binding.rvApprovalReport.adapter = dataRVAdapter
        binding.rvApprovalReport.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        // 클릭 이벤트 처리
        dataRVAdapter.setOnItemClickListener(object: ApprovalReportRVAdapter.OnItemClickListner {
            override fun onItemClick(v: View, data: ApprovalReport, pos: Int) {
                Log.d("로그", "결재 보고서 클릭, pos: $pos")
            }
        })
    }
}