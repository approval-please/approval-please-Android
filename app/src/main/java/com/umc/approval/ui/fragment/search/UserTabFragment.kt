package com.umc.approval.ui.fragment.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.FragmentSearchUserTabBinding
import com.umc.approval.ui.adapter.participant_activity.ParticipantRVAdapter
import com.umc.approval.util.Participant

class UserTabFragment: Fragment() {

    private var _binding : FragmentSearchUserTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchUserTabBinding.inflate(inflater, container, false)
        val view = binding.root

        setLikeList()  // 리사이클러뷰 아이템 & 어댑터 설정

        return view
    }

    private fun setLikeList() {
        val like: ArrayList<Participant> = arrayListOf()

        like.apply {
            add(Participant("", "부장", "닉네임1", true))
            add(Participant("", "차장", "닉네임2", false))
            add(Participant("", "부장", "닉네임3", false))
            add(Participant("", "부장", "닉네임4", true))
            add(Participant("", "부장", "닉네임5", false))
            add(Participant("", "부장", "닉네임6", true))
            add(Participant("", "부장", "닉네임7", true))
            add(Participant("", "차장", "닉네임8", false))
        }

        val participantRVAdapter = ParticipantRVAdapter(like)
        val spaceDecoration = VerticalSpaceItemDecoration(90)
        binding.rvLike.addItemDecoration(spaceDecoration)
        binding.rvLike.adapter = participantRVAdapter
        binding.rvLike.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
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