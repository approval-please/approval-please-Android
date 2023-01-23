package com.umc.approval.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.umc.approval.R
import com.umc.approval.databinding.ActivityParticipantBinding
import com.umc.approval.ui.fragment.participant.AgreeFragment
import com.umc.approval.ui.fragment.participant.RejectFragment

class ParticipantActivity : AppCompatActivity() {
    lateinit var binding: ActivityParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParticipantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        // 데이터 받아와서 승인, 반려자 수 설정

        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameFragment.id, AgreeFragment())
            .commitAllowingStateLoss()

        binding.btnAgreeFragment.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(binding.frameFragment.id, AgreeFragment())
                .commitAllowingStateLoss()

            binding.ivAgreeIcon.setImageResource(R.drawable.approval_fragment_approve_icon)
            binding.tvAgreeText.setTextColor(Color.BLACK)
            binding.tvAgreeCount.setTextColor(Color.BLACK)
            binding.ivRejectIcon.setImageResource(R.drawable.participant_activity_rejected_icon_gray)
            binding.tvRejectText.setTextColor(ContextCompat.getColor(this, R.color.gray_text_color))
            binding.tvRejectCount.setTextColor(ContextCompat.getColor(this, R.color.gray_text_color))
        }

        binding.btnRejectFragment.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(binding.frameFragment.id, RejectFragment())
                .commitAllowingStateLoss()

            binding.ivRejectIcon.setImageResource(R.drawable.approval_fragment_reject_icon)
            binding.tvRejectText.setTextColor(Color.BLACK)
            binding.tvRejectCount.setTextColor(Color.BLACK)
            binding.ivAgreeIcon.setImageResource(R.drawable.participant_activity_approved_icon_gray)
            binding.tvAgreeText.setTextColor(ContextCompat.getColor(this, R.color.gray_text_color))
            binding.tvAgreeCount.setTextColor(ContextCompat.getColor(this, R.color.gray_text_color))
        }
    }
}