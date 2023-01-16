package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ActivityInterestingDepartmentBinding
import com.umc.approval.ui.adapter.interesting_activity.InterestingDepartmentRVAdapter
import com.umc.approval.ui.adapter.interesting_activity.UninterestingDepartmentRVAdapter

class InterestingDepartmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityInterestingDepartmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterestingDepartmentBinding.inflate(layoutInflater)
        val view = binding.root

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        // 모든 부서와 관심 부서 데이터
        val interestingDepartment = arrayListOf(
            "디지털 기기",
            "생활 가전",
            "생활 용품",
        )

        val department = arrayListOf(
            "가구 / 인테리어",
            "주방 / 건강",
            "출산 / 유아동",
            "패션 의류 / 잡화",
            "뷰티 / 미용",
            "스포츠 / 레저 / 헬스",
            "취미 / 게임 / 완구",
            "문구 / 오피스",
            "도서 / 음악",
            "티켓 / 교환권",
            "식품",
            "동물 / 식물",
            "영화 / 공연",
            "자동차 / 공구",
            "기타 물품",
        )

        /* 리사이클러뷰 어댑터 설정 */
        // 관심 부서
        val rvAdapter = InterestingDepartmentRVAdapter(interestingDepartment)
        binding.rvInterestingDepartment.adapter = rvAdapter
        binding.rvInterestingDepartment.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        // 관심 부서 아이템 이동 콜백 변수
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback (
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.DOWN
                ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos: Int = viewHolder.adapterPosition
                val toPos: Int = target.adapterPosition

                rvAdapter.swapData(fromPos, toPos)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 스와이프 동작 없음
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.rvInterestingDepartment)

        // 모든 부서(관심 부서 제외)
        binding.rvUninterestingDepartment.adapter = UninterestingDepartmentRVAdapter(department)
        binding.rvUninterestingDepartment.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        setContentView(view)
    }
}