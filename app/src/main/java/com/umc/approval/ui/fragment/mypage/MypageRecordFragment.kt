package com.umc.approval.ui.fragment.mypage

import com.umc.approval.ui.adapter.mypage_fragment.MyPageRecordAdapter
import com.umc.approval.ui.adapter.mypage_fragment.MyPageRecordItem
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.approval.databinding.FragmentMypageRecordBinding

/**
 * MyPage 실적 tab View
 * */
class MypageRecordFragment : Fragment() {

    private var _binding : FragmentMypageRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageRecordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()

        binding.mypageRecordRecyclerview.layoutManager = LinearLayoutManager(this.context)
        val itemList = ArrayList<MyPageRecordItem>()
        itemList.add(MyPageRecordItem("어제", "서류 작성", "+100"))
        itemList.add(MyPageRecordItem("22.12.31", "결재 보고서 작성", "+2100"))
        for (i in 1..10){
            itemList.add(MyPageRecordItem("22.12.31", "서류 댓글 작성 테스트", "+10000"))
        }

        val mypageRecordAdapter = MyPageRecordAdapter(itemList)
        mypageRecordAdapter.notifyDataSetChanged()

        binding.mypageRecordRecyclerview.adapter = mypageRecordAdapter
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}