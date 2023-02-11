package com.umc.approval.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.check.collie.OtherpageActivity
import com.umc.approval.data.dto.community.get.ParticipantDto
import com.umc.approval.data.dto.community.get.VoteParticipantDto
import com.umc.approval.databinding.ActivityCommunityTokVoteParticipantBinding
import com.umc.approval.ui.adapter.community_fragment.VoteParticipantRVAdapter
import com.umc.approval.ui.viewmodel.community.VoteParticipantViewModel
import com.umc.approval.ui.viewmodel.communityDetail.TokViewModel
import com.umc.approval.ui.viewmodel.follow.FollowViewModel

class CommunityTokVoteParticipant : AppCompatActivity() {
    lateinit var binding: ActivityCommunityTokVoteParticipantBinding

    private val voteViewModel by viewModels<VoteParticipantViewModel>()
    private val viewModel by viewModels<TokViewModel>()

    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityTokVoteParticipantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        if (viewModel.accessToken.value == false) {
//            BlackToast(this, "로그인 과정이 필요합니다").show()
            finish()
        }

        val title = intent.getStringExtra("title")
        binding.voteItemTitle.text = title
        val voteOpt = intent.getIntExtra("voteId",-1)
        voteViewModel.get_community_tok_vote_participant(voteOpt)

        setLikeList()
    }


    private fun setLikeList() {

        //팔로우 상태 변화면 재 요청
        followViewModel.isFollow.observe(this) {
            val voteOpt = intent.getIntExtra("voteId",-1)
            voteViewModel.get_community_tok_vote_participant(voteOpt)
        }

        voteViewModel.participantList.observe(this) {
            val participantRVAdapter = VoteParticipantRVAdapter(it?: VoteParticipantDto(listOf()))
            binding.rvLike.adapter = participantRVAdapter
            binding.rvLike.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            participantRVAdapter.itemClick = object : VoteParticipantRVAdapter.ItemClick{
                override fun move_to_profile(v: View, data: ParticipantDto, pos: Int) {
                    val intent = Intent(baseContext, OtherpageActivity::class.java)
//                    intent.putExtra("userId", data.)
                    startActivity(intent)
                }

                override fun follow(v: View, data: ParticipantDto, pos: Int) {
//                    followViewModel.follow(data.userId)
                }

                override fun unfollow(v: View, data: ParticipantDto, pos: Int) {
//                    followViewModel.follow(data.userId)
                }
            }
        }
    }
}