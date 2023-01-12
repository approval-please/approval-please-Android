package com.umc.approval.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.FragmentRecentSearchBinding
import com.umc.approval.ui.adapter.RecentSearchRVAdapter

/**
 * 최근 검색어 View
 * */
class RecentSearchFragment : Fragment() {

    private var _binding : FragmentRecentSearchBinding? = null
    private val binding get() = _binding!!

    //최근 검색어 RV Adapter
    private lateinit var recentSearchRVAdapter_1: RecentSearchRVAdapter
    private lateinit var recentSearchRVAdapter_2: RecentSearchRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**Recycler View*/
    private fun setupRecyclerView() {

        val initList_1 = mutableListOf<String>()
        initList_1.add("아이폰 14")
        initList_1.add("전자기기")
        initList_1.add("냉장고")
        initList_1.add("캠핑 용품")

        val initList_2 = mutableListOf<String>()
        initList_2.add("체크카드")
        initList_2.add("여름용 반바지")
        initList_2.add("겨울용 코트")
        initList_2.add("키보드")

        recentSearchRVAdapter_1 = RecentSearchRVAdapter(initList_1)

        recentSearchRVAdapter_2 = RecentSearchRVAdapter(initList_2)

        val recent_search_rv_1 : RecyclerView = binding.recentSearchRv1
        recent_search_rv_1.adapter = recentSearchRVAdapter_1
        recent_search_rv_1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recent_search_rv_2 : RecyclerView = binding.recentSearchRv2
        recent_search_rv_2.adapter = recentSearchRVAdapter_2
        recent_search_rv_2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
}