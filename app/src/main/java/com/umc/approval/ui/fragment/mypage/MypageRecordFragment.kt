package com.umc.approval.ui.fragment.mypage

import com.umc.approval.ui.adapter.mypage_fragment.MyPageRecordAdapter
import com.umc.approval.ui.adapter.mypage_fragment.MyPageRecordItem
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.approval.databinding.FragmentMypageRecordBinding
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel

/**
 * MyPage 실적 tab View
 * */
class MypageRecordFragment : Fragment() {

    private var _binding : FragmentMypageRecordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageRecordBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.get_my_performances()

        binding.mypageRecordRecyclerview.layoutManager = LinearLayoutManager(this.context)

        viewModel.performances.observe(viewLifecycleOwner) {
            val mypageRecordAdapter = MyPageRecordAdapter(it)
            mypageRecordAdapter.notifyDataSetChanged()
            binding.mypageRecordRecyclerview.adapter = mypageRecordAdapter
        }

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