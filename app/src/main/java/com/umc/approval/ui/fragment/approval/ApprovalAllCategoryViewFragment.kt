package com.umc.approval.ui.fragment.approval

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentApprovalAllCategoryViewBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel
import com.umc.approval.ui.adapter.approval_fragment.CategoryRVAdapter
import com.umc.approval.util.InterestingCategory

class ApprovalAllCategoryViewFragment: Fragment() {

    private var _binding : FragmentApprovalAllCategoryViewBinding? = null
    private val binding get() = _binding!!

    /**mypage view model*/
    private val viewModel by viewModels<ApprovalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApprovalAllCategoryViewBinding.inflate(inflater, container, false)
        val view = binding.root

        //live data
        live_data()
        
        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_all_documents()

        setAllCategoryList()
    }

    override fun onResume() {
        super.onResume()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_all_documents()

        setAllCategoryList()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    //라이브 데이터
    private fun live_data() {

        //모든 목록 받아오는 라이브 데이터
        viewModel.approval_all_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperListRVAdapter(it)
            binding.rvApprovalPaper.adapter = dataRVAdapter
            binding.rvApprovalPaper.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            /**결재서류 클릭시 해당 결재서류로 이동*/
            dataRVAdapter.setOnItemClickListener(object :
                ApprovalPaperListRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {

                    /**결재서류 아이디를 넘김*/
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.documentId.toString())

                    startActivity(intent)
                }
            })
        }
    }

    //모든 카테고리 목록
    private fun setAllCategoryList() {
        val allCategory: ArrayList<InterestingCategory> = arrayListOf()  // 샘플 데이터

        allCategory.apply{
            add(InterestingCategory("모든 부서", true))
            add(InterestingCategory("디지털기기", false))
            add(InterestingCategory("생활가전", false))
            add(InterestingCategory("생활용품", false))
            add(InterestingCategory("가구/인테리어", false))
            add(InterestingCategory("주방/건강", false))
            add(InterestingCategory("출산/유아동", false))
            add(InterestingCategory("패션의류/잡화", false))
            add(InterestingCategory("뷰티/미용", false))
            add(InterestingCategory("스포츠/헬스/레저", false))
            add(InterestingCategory("취미/게임/완구", false))
            add(InterestingCategory("문구/오피스", false))
            add(InterestingCategory("도서/음악", false))
            add(InterestingCategory("티켓/교환권", false))
            add(InterestingCategory("식품", false))
            add(InterestingCategory("동물/식물", false))
            add(InterestingCategory("영화/공연", false))
            add(InterestingCategory("자동차/공구", false))
            add(InterestingCategory("기타 물품", false))
        }

        val categoryRVAdapter = CategoryRVAdapter(allCategory)
        binding.rvCategory.adapter = categoryRVAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        // 클릭 이벤트 처리
        categoryRVAdapter.setOnItemClickListener(object: CategoryRVAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: InterestingCategory, pos: Int) {
                var num = pos-1
                if (num == -1) {
                    viewModel.get_all_documents(null)
                } else {
                    viewModel.get_all_documents((pos-1).toString())
                }
            }
        })
    }
}