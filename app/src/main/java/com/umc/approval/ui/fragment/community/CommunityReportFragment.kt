package com.umc.approval.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.databinding.FragmentCommunityReportBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.viewmodel.community.CommunityReportViewModel

class CommunityReportFragment : Fragment() {

    private var _binding : FragmentCommunityReportBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityReportItemRVAdapter: CommunityReportItemRVAdapter

    private val viewModel by viewModels<CommunityReportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityReportBinding.inflate(inflater, container, false)
        val view = binding.root

        live_data()

        //아이템 선택 이벤트
        binding.hotCategory.setOnClickListener {
            viewModel.get_all_reports(0)
        }

        binding.followCategory.setOnClickListener {
            viewModel.get_all_reports(1)
        }

        binding.newCategory.setOnClickListener {
            viewModel.get_all_reports(3)
        }

        binding.myCategory.setOnClickListener {
            viewModel.get_all_reports(2)
        }

        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        viewModel.checkAccessToken()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.get_all_reports()
    }

    override fun onResume() {
        super.onResume()

        if (binding.hotCategory.isChecked) {
            viewModel.get_all_reports(0)
        } else if (binding.followCategory.isChecked) {
            viewModel.get_all_reports(1)
        } else if (binding.myCategory.isChecked) {
            viewModel.get_all_reports(2)
        } else {
            viewModel.get_all_reports(3)
        }
    }

    private fun live_data() {

        //엑세스 토큰 확인하는 라이브 데이터
        viewModel.accessToken.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.followCategory.isVisible = true
                binding.myCategory.isVisible = true
            } else {
                binding.followCategory.isVisible = false
                binding.myCategory.isVisible = false
            }
        }

        viewModel.report_list.observe(viewLifecycleOwner) {

            communityReportItemRVAdapter = CommunityReportItemRVAdapter(it)

            val community_item_rv: RecyclerView = binding.communityRvItem

            community_item_rv.adapter = communityReportItemRVAdapter
            community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            communityReportItemRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
                override fun move_to_report_activity(v: View, data: CommunityReport, pos: Int) {
                    //report Id 전달
                    val intent = Intent(requireContext(), CommunityReportActivity::class.java)
                    intent.putExtra("reportId", data.reportId.toString())
                    startActivity(intent)
                }
                override fun move_to_document_activity(v: View, data: CommunityReport, pos: Int) {
                    //report Id 전달
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.document.documentId.toString())
                    startActivity(intent)
                }
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