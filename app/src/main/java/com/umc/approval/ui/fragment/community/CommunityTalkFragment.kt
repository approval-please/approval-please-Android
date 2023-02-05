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
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.databinding.FragmentCommunityTalkBinding
import com.umc.approval.ui.activity.CommunityTokActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityTalkItemRVAdapter
import com.umc.approval.ui.viewmodel.community.CommunityTokViewModel

/**
 * 로직 체크 완료
 * */
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

        //아이템 선택 이벤트
        binding.hotCategory.setOnClickListener {
            viewModel.get_all_toks(0)
        }

        binding.followCategory.setOnClickListener {
            viewModel.get_all_toks(1)
        }

        binding.newCategory.setOnClickListener {
            viewModel.get_all_toks(3)
        }

        binding.myCategory.setOnClickListener {
            viewModel.get_all_toks(2)
        }

        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        viewModel.checkAccessToken()

        //모든 톡 목록 가지고오는 로직
        viewModel.get_all_toks()
    }

    override fun onResume() {
        super.onResume()

        if (binding.hotCategory.isChecked) {
            viewModel.get_all_toks(0)
        } else if (binding.followCategory.isChecked) {
            viewModel.get_all_toks(1)
        } else if (binding.myCategory.isChecked) {
            viewModel.get_all_toks(2)
        } else {
            viewModel.get_all_toks(3)
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

        viewModel.tok_list.observe(viewLifecycleOwner) {
            communityTalkItemRVAdapter = CommunityTalkItemRVAdapter(it)

            val community_item_rv: RecyclerView = binding.communityRvItem

            community_item_rv.adapter = communityTalkItemRVAdapter
            community_item_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            communityTalkItemRVAdapter.itemClick = object : CommunityTalkItemRVAdapter.ItemClick {
                override fun move_to_tok_activity(v: View, data: CommunityTok, pos: Int) {

                    //toktok Id 전달
                    val intent = Intent(requireContext(), CommunityTokActivity::class.java)
                    intent.putExtra("toktokId", data.toktokId.toString())
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