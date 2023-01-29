package com.umc.approval.ui.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.approval.databinding.FragmentSearchBottomSheetDialogCategoryBinding
import com.umc.approval.ui.adapter.search_fragment.CategoryDialogRVAdapter
import com.umc.approval.util.CategorySelectDialogItem

class SearchBottomSheetDialogCategoryFragment: BottomSheetDialogFragment(){
    private var _binding : FragmentSearchBottomSheetDialogCategoryBinding? = null
    private val binding get() = _binding!!

    private val categoryList: ArrayList<CategorySelectDialogItem> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomSheetDialogCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        setCategoryList()

        binding.btnApply.setOnClickListener {
            val selectedCategoryIndex: ArrayList<Int> = arrayListOf()

            for (i in 0 until categoryList.size) {
                if (categoryList[i].isChecked)
                    selectedCategoryIndex.add(i)
            }

            val result = "선택 부서"
            setFragmentResult("category", bundleOf("result" to result))

            setFragmentResult("categoryList", bundleOf("category" to selectedCategoryIndex))

            dismiss()
        }

        return view
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * 카테고리 선택 다이얼로그 아이템 설정
     */
    private fun setCategoryList() {
        categoryList.apply {
            add(CategorySelectDialogItem("디지털기기", false))
            add(CategorySelectDialogItem("생활가전", false))
            add(CategorySelectDialogItem("생활용품", false))
            add(CategorySelectDialogItem("가구/인테리어", false))
            add(CategorySelectDialogItem("주방/건강", false))
            add(CategorySelectDialogItem("출산/유아동", false))
            add(CategorySelectDialogItem("패션의류/잡화", false))
            add(CategorySelectDialogItem("뷰티/미용", false))
            add(CategorySelectDialogItem("스포츠/헬스/레저", false))
            add(CategorySelectDialogItem("취미/게임/완구", false))
            add(CategorySelectDialogItem("문구/오피스", false))
            add(CategorySelectDialogItem("도서/음악", false))
            add(CategorySelectDialogItem("티켓/교환권", false))
            add(CategorySelectDialogItem("식품", false))
            add(CategorySelectDialogItem("동물/식물", false))
            add(CategorySelectDialogItem("영화/공연", false))
            add(CategorySelectDialogItem("자동차/공구", false))
            add(CategorySelectDialogItem("기타 물품", false))
        }

        val divider = DividerItemDecoration(requireContext(), VERTICAL)
        binding.rvCategory.addItemDecoration(divider)
        val categoryDialogRVAdapter = CategoryDialogRVAdapter(categoryList)
        binding.rvCategory.adapter = categoryDialogRVAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}