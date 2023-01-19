package com.umc.approval.ui.fragment.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.CommunityItemDto
import com.umc.approval.databinding.FragmentCommunityAllBinding
import com.umc.approval.ui.adapter.community_fragment.CommunityItemRVAdapter
import com.umc.approval.ui.adapter.search_fragment.SearchIngRVAdapter

class CommunityAllFragment : Fragment() {

    private var _binding : FragmentCommunityAllBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityItemRVAdapter: CommunityItemRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityAllBinding.inflate(inflater, container, false)
        val view = binding.root

        val init_data = mutableListOf<CommunityItemDto>()

        init_data.add(CommunityItemDto("첫번째", "aaaa", "aaaa"))
        init_data.add(CommunityItemDto("두번째", "", "aaaa"))
        init_data.add(CommunityItemDto("세번째", "aaaa", ""))
        init_data.add(CommunityItemDto("네번째", "aaaa", "aaaa"))

        communityItemRVAdapter = CommunityItemRVAdapter(init_data)

        val recent_search_rv : RecyclerView = binding.communityRvItem

        recent_search_rv.adapter = communityItemRVAdapter
        recent_search_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        return view
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}