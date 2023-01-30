package com.umc.approval.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_all_reports()
    }

    override fun onResume() {
        super.onResume()

        viewModel.get_all_reports()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        if (binding.hotCategory.isChecked) {
            viewModel.get_all_reports(0)
        } else if (binding.followCategory.isChecked) {
            viewModel.get_all_reports(1)
        } else if (binding.myCategory.isChecked) {
            viewModel.get_all_reports(2)
        } else {
            viewModel.get_all_reports(0)
        }
    }

    private fun live_data() {

        viewModel.report_list.observe(viewLifecycleOwner) {

            communityReportItemRVAdapter = CommunityReportItemRVAdapter(it)

            val community_item_rv: RecyclerView = binding.communityRvReportItem

            community_item_rv.adapter = communityReportItemRVAdapter
            community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            communityReportItemRVAdapter.itemClick = object : CommunityReportItemRVAdapter.ItemClick {
                override fun move_to_report_activity() {
                    startActivity(Intent(requireContext(), CommunityReportActivity::class.java))
                }
                override fun move_to_document_activity() {
                    startActivity(Intent(requireContext(), DocumentActivity::class.java))
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