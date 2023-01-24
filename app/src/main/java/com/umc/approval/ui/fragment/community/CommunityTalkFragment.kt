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
import com.umc.approval.databinding.FragmentCommunityTalkBinding
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.viewmodel.community.CommunityViewModel

class CommunityTalkFragment : Fragment() {

    private var _binding : FragmentCommunityTalkBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityTalkItemRVAdapter: CommunityTalkItemRVAdapter

    private val viewModel by viewModels<CommunityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityTalkBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.init_all_toks()

        connect_to_community_rv()

        return view
    }

    private fun connect_to_community_rv() {

        viewModel.tok_list.observe(viewLifecycleOwner) {
            communityTalkItemRVAdapter = CommunityTalkItemRVAdapter(it)

            val community_item_rv: RecyclerView = binding.communityRvItem

            community_item_rv.adapter = communityTalkItemRVAdapter
            community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            communityTalkItemRVAdapter.itemClick = object : CommunityTalkItemRVAdapter.ItemClick {
                override fun move_to_tok_activity() {
                    startActivity(Intent(requireContext(), CommunityTokActivity::class.java))
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