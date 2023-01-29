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
import com.umc.approval.ui.activity.DocumentActivity
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

        var approvalPaper =
            com.umc.approval.data.dto.approval.get.ApprovalPaper(
                0, 0, "30분전",
                mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12
            )

        init_data.add(
            CommunityReport
                (
                "아란", "", approvalPaper,"내용내용내용내용", mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png","https://mblogthumb-phinf.pstatic.net/MjAxNzA3MjBfNjcg/MDAxNTAwNTQzMjg1ODk4.p5_6F3n3NH_QfSm_aM1E-TTRMAC0_U3i3zZD6KoTR38g.DElhqgeg6flB_k_ohCHUsROTLNFeQDxdfp1IvYkkAyAg.JPEG.fotolia_korea/%EA%B7%80%EC%97%AC%EC%9A%B4_%EA%B0%95%EC%95%84%EC%A7%80_%EC%82%AC%EC%A7%84_1.jpg?type=w800"), mutableListOf("태그","태그2"),
                mutableListOf(openGraphDto), 0, 0, 0, 0, "")
            )
        init_data.add(
            CommunityReport
                (
                "아란", "", approvalPaper,"내용내용내용내용", mutableListOf("https://mblogthumb-phinf.pstatic.net/MjAxNzA3MjBfNjcg/MDAxNTAwNTQzMjg1ODk4.p5_6F3n3NH_QfSm_aM1E-TTRMAC0_U3i3zZD6KoTR38g.DElhqgeg6flB_k_ohCHUsROTLNFeQDxdfp1IvYkkAyAg.JPEG.fotolia_korea/%EA%B7%80%EC%97%AC%EC%9A%B4_%EA%B0%95%EC%95%84%EC%A7%80_%EC%82%AC%EC%A7%84_1.jpg?type=w800"), mutableListOf("태그2"),
                mutableListOf(openGraphDto), 0, 0, 0, 0, "")
        )
        init_data.add(
            CommunityReport
                (
                "아란", "", approvalPaper,"내용내용내용내용", mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png","https://mblogthumb-phinf.pstatic.net/MjAxNzA3MjBfNjcg/MDAxNTAwNTQzMjg1ODk4.p5_6F3n3NH_QfSm_aM1E-TTRMAC0_U3i3zZD6KoTR38g.DElhqgeg6flB_k_ohCHUsROTLNFeQDxdfp1IvYkkAyAg.JPEG.fotolia_korea/%EA%B7%80%EC%97%AC%EC%9A%B4_%EA%B0%95%EC%95%84%EC%A7%80_%EC%82%AC%EC%A7%84_1.jpg?type=w800"), mutableListOf("태그","태그2"),
                mutableListOf(openGraphDto), 0, 0, 0, 0, "")
        )

        communityReportItemRVAdapter = CommunityReportItemRVAdapter(init_data)

        val community_item_rv: RecyclerView = binding.communityRvItem

        community_item_rv.adapter = communityReportItemRVAdapter
        community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        communityReportItemRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
            override fun move_to_report_activity() {
                startActivity(Intent(requireContext(), DocumentActivity::class.java))
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