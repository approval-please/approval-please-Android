package com.umc.approval.ui.fragment.search

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.umc.approval.databinding.FragmentRecentSearchBinding
import com.umc.approval.ui.activity.SearchActivity
import com.umc.approval.ui.adapter.search_fragment.RecentSearchRVAdapter
import com.umc.approval.ui.adapter.search_fragment.SearchIngRVAdapter
import com.umc.approval.data.dto.KeywordDto
import com.umc.approval.ui.viewmodel.search.RecentSearchViewModel

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
    lateinit var viewModel: RecentSearchViewModel

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

        viewModel = (activity as SearchActivity).viewModel

        /**검색 버튼 눌렀을때 이벤트 발생*/
        binding.search.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.addKeyword(KeywordDto(0, binding.search.text.toString(), "cswcsm02@gmail.com"))
                handled = true
            }
            handled
        }

        /**최근 검색어 전체 삭제*/
        binding.allDeleteText.setOnClickListener {
            viewModel.deleteAllKeyword()
        }

        /**최근 검색어 RecyclerView를 생성해주는 함수*/
        recent_keyword_recycler_view()

        /**연관검색어 RecyclerView를 생성해주는 함수*/
        related_keyword_recycler_view()

        /**텍스트 수정시 디바운스 적용 및 그 외 작업*/
        editText()
    }

    /**최근 검색어 RV를 초기화 및 최근 검색어 삭제 메서드*/
    private fun recent_keyword_recycler_view() {

        //초기화시 검색어를 가지고 오는 메소드
        viewModel.searchKeyword()

        //최근 검색어에 변경이 일어났을때 실행하는 메소드
        viewModel.recent_keyword.observe(viewLifecycleOwner) {

            recentSearchRVAdapter = RecentSearchRVAdapter(it)

            val recent_search_rv : RecyclerView = binding.recentSearchRv
            recent_search_rv.adapter = recentSearchRVAdapter

            //4개보다 적은 경우는 LinearLayoutManager, 4개보다 많은 경우 StaggeredGridLayoutManager 사용
            if (it.size <= 4) {
                recent_search_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            } else {
                recent_search_rv.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
            }

            //개별 검색어 클릭 이벤트
            recentSearchRVAdapter.itemClick = object : RecentSearchRVAdapter.ItemClick {
                //키워드 삭제
                override fun keyword_remove(view: View, keyword: KeywordDto) {
                    viewModel.deleteKeyword(keyword)
                }
                //키워드 검색
                override fun search(view: View, keyword: KeywordDto) {
                }
            }
        }
    }

    /**연관 검색어 RV를 생성해주는 메소드*/
    private fun related_keyword_recycler_view() {

        //연관 검색어에 변경이 일어났을때 실행하는 메소드
        viewModel.related_keyword.observe(viewLifecycleOwner) {

            searchIngRVAdapter = SearchIngRVAdapter(it)

            val recent_search_rv : RecyclerView = binding.recentSearchRv

            recent_search_rv.adapter = searchIngRVAdapter
            recent_search_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            //개별 검색어 클릭 이벤트
            searchIngRVAdapter.itemClick = object : SearchIngRVAdapter.ItemClick {

                //연관 검색어 탐색
                override fun related_keyword_search(view: View, keyword: KeywordDto) {
                }
            }
        }
    }

    /**디바운스를 적용해 검색어 변화에 따라 쿼리를 날리는 메소드*/
    private fun editText() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        //addTextChangedListener는 editText속성을 가지는데 값이 변할때마다 viewModel로 결과가 전달
        binding.search.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            //처음 입력과 두번째 입력 사이의 차이가 100M초를 넘을때 실행
            if (endTime - startTime >= 100L) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        viewModel.relatedKeyword(query)
                        binding.allDeleteText.isVisible = false
                        binding.recentText.isVisible = false
                    } else {
                        viewModel.searchKeyword()
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