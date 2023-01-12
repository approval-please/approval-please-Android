package com.umc.approval.ui.fragment.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.umc.approval.databinding.FragmentRecentSearchBinding
import com.umc.approval.ui.adapter.RecentSearchRVAdapter
import com.umc.approval.ui.adapter.SearchIngRVAdapter
import com.umc.approval.ui.viewmodel.RecentSearchViewModel

/**
 * 최근 검색어 View
 * */
class RecentSearchFragment : Fragment() {

    private var _binding : FragmentRecentSearchBinding? = null
    private val binding get() = _binding!!

    //최근 검색어 RV Adapter
    private lateinit var recentSearchRVAdapter: RecentSearchRVAdapter
    private lateinit var searchIngRVAdapter: SearchIngRVAdapter

    //RecentSearch View Model
    private val viewModel by viewModels<RecentSearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        /**RecyclerView를 생성해주는 함수*/
        setupRecyclerView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**Keyword를 생성해주는 함수*/
        searchKeyword()

        viewModel.search_text.observe(viewLifecycleOwner) {

            if (!it.isEmpty()) {
                searchIngRVAdapter = SearchIngRVAdapter(it)

                val recent_search_rv : RecyclerView = binding.recentSearchRv
                recent_search_rv.adapter = searchIngRVAdapter
                recent_search_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            } else {

                val initList = mutableListOf<String>()
                initList.add("아이폰 14")
                initList.add("여름용 반바지")
                initList.add("전자기기")
                initList.add("냉장고")
                initList.add("겨울용 코트")
                initList.add("캠핑 용품")
                initList.add("체크카드")
                initList.add("키보드")

                recentSearchRVAdapter = RecentSearchRVAdapter(initList)

                val recent_search_rv : RecyclerView = binding.recentSearchRv
                recent_search_rv.adapter = recentSearchRVAdapter
                recent_search_rv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
            }
        }
    }

    /**Recycler View*/
    private fun setupRecyclerView() {

        val initList = mutableListOf<String>()
        initList.add("아이폰 14")
        initList.add("여름용 반바지")
        initList.add("전자기기")
        initList.add("냉장고")
        initList.add("겨울용 코트")
        initList.add("캠핑 용품")
        initList.add("체크카드")
        initList.add("키보드")

        recentSearchRVAdapter = RecentSearchRVAdapter(initList)

        val recent_search_rv : RecyclerView = binding.recentSearchRv
        recent_search_rv.adapter = recentSearchRVAdapter
        recent_search_rv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
    }

    /**디바운스를 적용해 검색어 변화에 따라 쿼리를 날리는 메소드*/
    private fun searchKeyword() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        //addTextChangedListener는 editText속성을 가지는데 값이 변할때마다 viewModel로 결과가 전달
        binding.search.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            //처음 입력과 두번째 입력 사이의 차이가 100M초를 넘을때 실행
            if (endTime - startTime >= 100L) {
                text?.let {
                    val query = it.toString().trim()
                    viewModel.searchKeyword(query)
                    if (query.isNotEmpty()) {
                        binding.allDeleteText.isVisible = false
                        binding.recentText.isVisible = false
                    } else {
                        binding.allDeleteText.isVisible = true
                        binding.recentText.isVisible = true
                    }
                }
            }
            startTime = endTime
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