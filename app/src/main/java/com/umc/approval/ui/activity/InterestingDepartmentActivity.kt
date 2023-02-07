package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.search.post.KeywordDto
import com.umc.approval.databinding.ActivityInterestingDepartmentBinding
import com.umc.approval.ui.adapter.approval_fragment.CategoryRVAdapter
import com.umc.approval.ui.adapter.interesting_activity.InterestingDepartmentRVAdapter
import com.umc.approval.ui.adapter.interesting_activity.UninterestingDepartmentRVAdapter
import com.umc.approval.ui.adapter.search_fragment.RecentSearchRVAdapter
import com.umc.approval.ui.viewmodel.approval.ApprovalViewModel
import com.umc.approval.ui.viewmodel.approval.InterestingViewModel
import com.umc.approval.util.InterestingCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InterestingDepartmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityInterestingDepartmentBinding

    /**관심부서 view model 초기화*/
    private val viewModel by viewModels<InterestingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterestingDepartmentBinding.inflate(layoutInflater)
        val view = binding.root

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        val department = listOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17)
        viewModel.setAll(department)

        //관심부서 목록 가지고 오기
        viewModel.interesting.observe(this) {

            val rvAdapter = InterestingDepartmentRVAdapter(it)
            binding.rvInterestingDepartment.adapter = rvAdapter
            binding.rvInterestingDepartment.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

            //관심부서 해제
            rvAdapter.itemClick = object : InterestingDepartmentRVAdapter.ItemClick {
                override fun category_select(view: View, int: Int) {
                    viewModel.post_interest(int, false)
                }
            }

            CoroutineScope(Dispatchers.IO).launch {

                val list = mutableListOf<Int>()
                for (i in viewModel.all.value!!) {
                    if (i !in viewModel.interesting.value!!) {
                        list.add(i)
                    }
                }
                viewModel.setNot(list)
            }
        }

        viewModel.not_interesting.observe(this) {

            val rvAdapter = UninterestingDepartmentRVAdapter(it)
            binding.rvUninterestingDepartment.adapter = rvAdapter
            binding.rvUninterestingDepartment.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

            //관심 부서로 선택
            rvAdapter.itemClick = object : UninterestingDepartmentRVAdapter.ItemClick {
                override fun category_select(view: View, int: Int) {
                    viewModel.post_interest(int, true)
                }
            }
        }

        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        //관심부서 서류 가지고 오기
        viewModel.get_interest()
    }
}