package com.umc.approval.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.check.collie.OtherpageActivity
import com.umc.approval.data.dto.common.CommonUserDto
import com.umc.approval.databinding.ActivityLikeBinding
import com.umc.approval.ui.adapter.like_activity.LikeRVAdapter
import com.umc.approval.ui.viewmodel.follow.FollowViewModel
import com.umc.approval.ui.viewmodel.like.LikeViewModel

class LikeActivity : AppCompatActivity() {
    lateinit var binding: ActivityLikeBinding

    private val viewModel by viewModels<LikeViewModel>()

    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        live_data()

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        viewModel.accessToken.observe(this) {
            // 받아온 인텐트에서 종류(서류/톡톡/보고서), id 가져오기
            val type = intent.getStringExtra("type")
            val id = intent.getIntExtra("id", -1)

            when (type) {
                "document" -> viewModel.get_paper_like_users(id)
                "toktok" -> viewModel.get_toktok_like_users(id)
                "report" -> viewModel.get_report_like_users(id)
            }
        }
    }

    // 시작 시 로그인 상태 검증 및 좋아요 누른 유저 목록 조회
    override fun onStart() {
        super.onStart()

        // 로그인 상태인지 확인
        viewModel.checkAccessToken()
    }
    private fun live_data() {

        followViewModel.isFollow.observe(this) {
            val type = intent.getStringExtra("type")
            val id = intent.getIntExtra("id", -1)
            when (type) {
                "document" -> viewModel.get_paper_like_users(id)
                "toktok" -> viewModel.get_toktok_like_users(id)
                "report" -> viewModel.get_report_like_users(id)
            }
        }

        // 서버에서 데이터를 받아오면 뷰에 적용하는 라이브 데이터
        viewModel.likeList.observe(this) {
            val likeRVAdapter = LikeRVAdapter(it)
            binding.rvLike.adapter = likeRVAdapter
            binding.rvLike.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            likeRVAdapter.itemClick = object : LikeRVAdapter.ItemClick{
                override fun move_to_profile(v: View, data: CommonUserDto, pos: Int) {
                    val intent = Intent(baseContext, OtherpageActivity::class.java)
                    intent.putExtra("userId", data.userId)
                    startActivity(intent)
                }

                override fun follow(v: View, data: CommonUserDto, pos: Int) {
                    followViewModel.follow(data.userId)
                }

                override fun unfollow(v: View, data: CommonUserDto, pos: Int) {
                    followViewModel.follow(data.userId)
                }
            }
        }
    }
}