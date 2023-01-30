package com.umc.approval.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.umc.approval.ui.viewmodel.community.CommunityTokViewModel

class CommunityTalkFragment : Fragment() {

    private var _binding : FragmentCommunityTalkBinding? = null
    private val binding get() = _binding!!

    //Community Image RV Adapter
    private lateinit var communityTalkItemRVAdapter: CommunityTalkItemRVAdapter

    private val viewModel by viewModels<CommunityTokViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityTalkBinding.inflate(inflater, container, false)
        val view = binding.root

        live_data()



        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        viewModel.get_all_toks()
    }

    override fun onResume() {
        super.onResume()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        if (binding.hotCategory.isChecked) {
            viewModel.get_all_toks(0)
        } else if (binding.followCategory.isChecked) {
            viewModel.get_all_toks(1)
        } else if (binding.myCategory.isChecked) {
            viewModel.get_all_toks(2)
        } else {
            viewModel.get_all_toks(0)
        }
    }

    private fun live_data() {

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