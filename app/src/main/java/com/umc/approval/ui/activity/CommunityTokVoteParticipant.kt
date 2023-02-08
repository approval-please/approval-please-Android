package com.umc.approval.ui.activity

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.ActivityCommunityTokVoteParticipantBinding
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.adapter.community_fragment.VoteParticipantRVAdapter
import com.umc.approval.ui.viewmodel.community.VoteParticipantViewModel
import com.umc.approval.ui.viewmodel.communityDetail.TokViewModel
import com.umc.approval.util.Participant

class CommunityTokVoteParticipant : AppCompatActivity() {
    lateinit var binding: ActivityCommunityTokVoteParticipantBinding

    private val voteViewModel by viewModels<VoteParticipantViewModel>()
    private val viewModel by viewModels<TokViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityTokVoteParticipantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        if (viewModel.accessToken.value == false) {
            Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
            finish()
        }

        val title = intent.getStringExtra("title")
        binding.voteItemTitle.text = title

        val voteOpt = intent.getIntExtra("voteId",-1)

        setLikeList(voteOpt)
    }


    private fun setLikeList(voteOpt:Int) {

        voteViewModel.get_community_tok_vote_participant(voteOpt)

        voteViewModel.participantList.observe(this) {
            val participantRVAdapter = VoteParticipantRVAdapter(it)
            binding.rvLike.adapter = participantRVAdapter
            binding.rvLike.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }
    }

}