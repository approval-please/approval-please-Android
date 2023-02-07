package com.umc.approval.ui.fragment.approval

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentApprovalInterestingCategoryViewBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.activity.InterestingDepartmentActivity
import com.umc.approval.ui.activity.LoginActivity
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.approval_fragment.CategoryRVAdapter
import com.umc.approval.ui.viewmodel.approval.ApprovalCommonViewModel
import com.umc.approval.util.BlackToast
import com.umc.approval.util.InterestingCategory
import com.umc.approval.util.Utils

class ApprovalInterestingCategoryViewFragment: Fragment() {
    private var _binding : FragmentApprovalInterestingCategoryViewBinding? = null
    private val binding get() = _binding!!

    /**approval view model 초기화*/
    private val viewModel by viewModels<ApprovalViewModel>()

    /**쿼리를 위한 뷰모델*/
    private val commonViewModel: ApprovalCommonViewModel by viewModels({requireParentFragment()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApprovalInterestingCategoryViewBinding.inflate(inflater, container, false)
        val view = binding.root

        //live data
        live_data()

        binding.addInterestCategoryButton.setOnClickListener {
            Log.d("로그", "관심 부서 추가 버튼 클릭")
            val intent = Intent(requireContext(), InterestingDepartmentActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_interesting_documents(
            viewModel.category.value, commonViewModel.state.value, commonViewModel.sortBy.value)

        viewModel.get_interest()
    }

    override fun onResume() {
        super.onResume()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.setCategory(18)

        viewModel.get_interesting_documents(
            null, commonViewModel.state.value, commonViewModel.sortBy.value)

        viewModel.get_interest()
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

        //상태 변화에 따른 목록 받아오는 라이브 데이터
        commonViewModel.state.observe(viewLifecycleOwner) {
            viewModel.get_interesting_documents(
                viewModel.category.value, commonViewModel.state.value, commonViewModel.sortBy.value)
        }

        commonViewModel.sortBy.observe(viewLifecycleOwner) {
            viewModel.get_interesting_documents(
                viewModel.category.value, commonViewModel.state.value, commonViewModel.sortBy.value)
        }

        //엑세스 토큰이 없으면 로그인 화면으로 이동
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (!it) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                BlackToast.createToast(requireContext(), "로그인이 필요한 서비스 입니다").show()
            }
        }

        //관심 결재 서류 가져오는 라이브 데이터
        viewModel.approval_interest_list.observe(viewLifecycleOwner) {
            val dataRVAdapter = ApprovalPaperListRVAdapter(it)
            binding.rvApprovalPaper.adapter = dataRVAdapter
            binding.rvApprovalPaper.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: ApprovalPaperListRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    /**결재서류 아이디를 넘김*/
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.documentId.toString())

                    startActivity(intent)
                }
            })
        }

        //카테고리 목록 받아오는 라이브 데이터
        viewModel.interesting.observe(viewLifecycleOwner) {

            val interestingCategory: ArrayList<InterestingCategory> = arrayListOf()  // 샘플 데이터

            interestingCategory.apply{
                add(InterestingCategory("관심 부서 전체", true))
            }


            for (i in it) {
                interestingCategory.apply{
                    add(InterestingCategory(Utils.categoryMap[i].toString(), false))
                }
            }

            val categoryRVAdapter = CategoryRVAdapter(interestingCategory)
            binding.rvCategory.adapter = categoryRVAdapter
            binding.rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            // 클릭 이벤트 처리
            categoryRVAdapter.setOnItemClickListener(object: CategoryRVAdapter.OnItemClickListener {
                override fun onItemClick(v: View, data: InterestingCategory, pos: Int) {
                    Log.d("로그", "카테고리 선택, pos: $pos, data: $data")
                    if (data.category in Utils.categoryMapReverse) {
                        viewModel.setCategory(Utils.categoryMapReverse.get(data.category)!!.toInt())
                        Handler(Looper.myLooper()!!).postDelayed({
                            viewModel.get_interesting_documents(
                                viewModel.category.value, commonViewModel.state.value, commonViewModel.sortBy.value)
                        }, 100)
                    } else {
                        viewModel.setCategory(18)
                        Handler(Looper.myLooper()!!).postDelayed({
                            viewModel.get_interesting_documents(
                                viewModel.category.value, commonViewModel.state.value, commonViewModel.sortBy.value)
                        }, 100)
                    }
                }
            })
        }
    }
}