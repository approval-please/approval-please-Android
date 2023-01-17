package com.umc.approval.ui.fragment.participant

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.FragmentRejectBinding
import com.umc.approval.ui.adapter.participant_activity.ParticipantRVAdapter
import com.umc.approval.util.Participant

class RejectFragment: Fragment() {
    private var _binding : FragmentRejectBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRejectBinding.inflate(inflater, container, false)
        val view = binding.root

        setParticipantList()  // 리사이클러뷰 데이터 & 어댑터 설정

        return view
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setParticipantList() {
        val rejectParticipant: ArrayList<Participant> = arrayListOf()

        rejectParticipant.apply {
            add(Participant("", "부장", "반려", true))
            add(Participant("", "차장", "닉네임2", false))
            add(Participant("", "부장", "닉네임3", false))
            add(Participant("", "부장", "닉네임4", true))
            add(Participant("", "부장", "닉네임5", false))
            add(Participant("", "부장", "닉네임6", true))
        }

        val participantRVAdapter = ParticipantRVAdapter(rejectParticipant)
        val spaceDecoration = VerticalSpaceItemDecoration(70)
        binding.rvParticipant.addItemDecoration(spaceDecoration)
        binding.rvParticipant.adapter = participantRVAdapter
        binding.rvParticipant.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    // 아이템 간 간격 조절 기능
    inner class VerticalSpaceItemDecoration(private val height: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = height
        }
    }
}