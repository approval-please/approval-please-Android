package com.umc.approval.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.FragmentCommunityReportBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter

class CommunityReportFragment : Fragment() {

    private var _binding : FragmentCommunityReportBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityReportItemRVAdapter: CommunityReportItemRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityReportBinding.inflate(inflater, container, false)
        val view = binding.root

        connect_to_community_rv()

        return view
    }

    private fun connect_to_community_rv() {
        val init_data = mutableListOf<CommunityReport>()

        var openGraphDto = OpenGraphDto(
            "https://www.naver.com/",
            "네이버",
            "네이버",
            "네이버",
            "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"
        )
        init_data.add(
            CommunityReport
                (
                "", "", "", "", "", mutableListOf(), mutableListOf(),
                openGraphDto, 0, 0, 0, 0, "")
            )

        init_data.add(
            CommunityReport
                (
                "", "", "", "", "", mutableListOf(), mutableListOf(),
                openGraphDto, 0, 0, 0, 0, "")
        )

        init_data.add(
            CommunityReport
                (
                "", "", "", "", "", mutableListOf(), mutableListOf(),
                openGraphDto, 0, 0, 0, 0, "")
        )

        communityReportItemRVAdapter = CommunityReportItemRVAdapter(init_data)

        val community_item_rv: RecyclerView = binding.communityRvItem

        community_item_rv.adapter = communityReportItemRVAdapter
        community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        communityReportItemRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
            override fun move_to_report_activity() {
                startActivity(Intent(requireContext(), CommunityReportActivity::class.java))
            }
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}