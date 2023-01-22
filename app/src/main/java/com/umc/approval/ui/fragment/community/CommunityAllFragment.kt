package com.umc.approval.ui.fragment.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.community.CommunityItemDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.FragmentCommunityAllBinding
import com.umc.approval.ui.adapter.community_fragment.CommunityItemRVAdapter

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

        connect_to_community_rv()

        return view
    }

    private fun connect_to_community_rv() {
        val init_data = mutableListOf<CommunityItemDto>()

        var openGraphDto = OpenGraphDto("https://www.naver.com/", "네이버", "네이버", "네이버", "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png")
        init_data.add(
            CommunityItemDto
                (
                0, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                1, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                0, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                1, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                0, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                1, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                0, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )
        init_data.add(
            CommunityItemDto
                (
                0, "", "", "", "", "", "",
                "", "", "", "", mutableListOf(), openGraphDto, 0, 0, 0, 0
            )
        )

        communityItemRVAdapter = CommunityItemRVAdapter(init_data)

        val community_item_rv: RecyclerView = binding.communityRvItem

        community_item_rv.adapter = communityItemRVAdapter
        community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}