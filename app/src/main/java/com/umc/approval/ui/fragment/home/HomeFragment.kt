package com.umc.approval.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.umc.approval.ui.activity.*
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
import com.umc.approval.util.Utils.categoryMap
import com.umc.approval.util.Utils.categoryMapReverse
import me.relex.circleindicator.CircleIndicator3

/**
 * Home Fragment
 */
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var bannerPosition = 0

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

        //다른 뷰로 이동하는 로직
        move_to_other_view()

        //live data
        live_data_from_server()
        
        //관심부서 탭으로 이동
        setting_interesting()

        //인기 최신 선택
        best_new_click()

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

        setBannerImage()
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        //전체 서류 가지고오는 로직
        approvalViewModel.get_all_documents(sortBy = "0")

        //관신 서류 가지고오는 로직
        approvalViewModel.get_interesting_documents()

        //tok 서류 가지고오는 로직
        tokViewModel.get_all_toks()

        //report 서류 가지고오는 로직
        reportViewModel.get_all_reports()

        //카테고리
        approvalViewModel.get_interest()
    }

    //인기 최신 순으로 서류 목록 가져오기
    private fun best_new_click() {
        //인기순
        binding.best.setOnClickListener {
            approvalViewModel.get_all_documents(sortBy = "0")
        }

        //최신 순
        binding.latest.setOnClickListener {
            approvalViewModel.get_all_documents()
        }
    }

    //관심부서 셋팅
    private fun setting_interesting() {
        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심 부서 추가 버튼 클릭")
            val intent = Intent(requireContext(), InterestingDepartmentActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**live data*/
    private fun live_data_from_server() {

        //엑세스 토큰 확인하는 라이브 데이터
        approvalViewModel.accessToken.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.notLoginStatus.isVisible = false
                binding.loginStatus.isVisible = true
            } else {
                binding.notLoginStatus.isVisible = true
                binding.loginStatus.isVisible = false
            }
        }

        //관심부터 데이터 받아오는 라이브 데이터
        approvalViewModel.approval_interest_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperRVAdapter(it)
            binding.rvMyInterestingPaper.adapter = dataRVAdapter
            binding.rvMyInterestingPaper.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalPaperRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {

                    //결재 서류 아이디를 통해 상세보기로 이동
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.documentId.toString())

                    startActivity(intent)
                }
            })
        }

        //전체부서 데이터 받아오는 라이브 데이터
        approvalViewModel.approval_all_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperRVAdapter(it)
            binding.rvApprovalPaper.adapter = dataRVAdapter
            binding.rvApprovalPaper.layoutManager = GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalPaperRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {

                    //결재 서류 아이디를 통해 상세보기로 이동
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.documentId.toString())

                    startActivity(intent)
                }
            })
        }

        //톡 목록 데이터 받아오는 라이브 데이터
        tokViewModel.tok_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = PopularPostRVAdapter(it)
            binding.rvPopularPost.adapter = dataRVAdapter
            binding.rvPopularPost.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: PopularPostRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: CommunityTok, pos: Int) {

                    //톡 아이디를 통해 상세보기로 이동
                    val intent = Intent(requireContext(), CommunityTokActivity::class.java)
                    intent.putExtra("toktokId", data.toktokId.toString())

                    startActivity(intent)
                }
            })
        }

        //리포트 목록 데이터 받아오는 라이브 데이터
        reportViewModel.report_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalReportRVAdapter(it)
            binding.rvApprovalReport.adapter = dataRVAdapter
            binding.rvApprovalReport.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalReportRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: CommunityReport, pos: Int) {

                    //리포트 아이디를 통해 상세보기로 이동
                    val intent = Intent(requireContext(), CommunityTokActivity::class.java)
                    intent.putExtra("reportId", data.reportId.toString())

                    startActivity(intent)
                }
            })
        }


        //카테고리 목록 받아오는 라이브 데이터
        approvalViewModel.interesting.observe(viewLifecycleOwner) {

            val interestingCategory: ArrayList<InterestingCategory> = arrayListOf()  // 샘플 데이터

            interestingCategory.apply{
                add(InterestingCategory("관심 부서 전체", true))
            }

            for (i in it) {
                interestingCategory.apply{
                    add(InterestingCategory(categoryMap[i].toString(), false))
                }
            }

            val categoryRVAdapter = CategoryRVAdapter(interestingCategory)
            binding.rvCategory.adapter = categoryRVAdapter
            binding.rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            categoryRVAdapter.setOnItemClickListener(object: CategoryRVAdapter.OnItemClickListener {
                override fun onItemClick(v: View, data: InterestingCategory, pos: Int) {
                    Log.d("로그", "카테고리 선택, pos: $pos, data: $data")
                    if (data.category in categoryMapReverse) {
                        approvalViewModel.get_interesting_documents(categoryMapReverse.get(data.category).toString())
                    } else {
                        approvalViewModel.get_interesting_documents(null)
                    }
                }
            })
        }
    }

    /**
     * 배너 이미지 어댑터 설정
     */
    private fun setBannerImage() {
        val photoUrlList = listOf(R.drawable.home_fragment_banner, R.drawable.home_fragment_banner_2, R.drawable.home_fragment_banner_3)

        bannerPosition = Int.MAX_VALUE / 2 - kotlin.math.ceil(photoUrlList.size.toDouble() / 2).toInt()
        binding.vpHomeBanner.setCurrentItem(bannerPosition, false)
        Log.d("로그", bannerPosition.toString())

        // 뷰페이저에 어댑터 연결
        val photoVPAdatper = BannerVPAdapter(photoUrlList)
        binding.vpHomeBanner.adapter = photoVPAdatper
        binding.vpHomeBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 인디케이터 생성 및 적용
        val indicator: CircleIndicator3 = binding.indicator
        indicator.createIndicators(photoUrlList.size, 0)

        binding.vpHomeBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            //사용자가 스크롤 했을때 position 수정
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.animatePageSelected(position % photoUrlList.size)
            }
        })
    }
}