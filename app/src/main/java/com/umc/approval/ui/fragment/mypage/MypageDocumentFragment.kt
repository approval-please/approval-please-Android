package com.umc.approval.ui.fragment.mypage

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.databinding.FragmentMypageDocumentBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogStatusFragment
import com.umc.approval.ui.viewmodel.mypage.MyPageApprovalViewModel
import com.umc.approval.ui.viewmodel.mypage.MypageViewModel
import com.umc.approval.util.Utils

/**
 * MyPage 결재 서류 tab View
 * */
class MypageDocumentFragment : Fragment() {

    private var _binding : FragmentMypageDocumentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyPageApprovalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageDocumentBinding.inflate(inflater, container, false)
        val view = binding.root

        //버튼 선택시 뷰모델에 데이터 저장하는 이벤트
        binding.write.setOnClickListener {
            viewModel.setApproved(2)
        }
        binding.approve.setOnClickListener {
            viewModel.setApproved(1)
        }
        binding.reject.setOnClickListener {
            viewModel.setApproved(0)
        }

        binding.stateSelect.setOnClickListener {
            val bottomSheetDialog = ApprovalBottomSheetDialogStatusFragment(binding.stateText.text.toString())
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }

        //상태 선택 시 뷰 모델에 데이터 저장
        childFragmentManager
            .setFragmentResultListener("status", this) { _, bundle ->
                val result = bundle.getString("result")
                viewModel.setState(Utils.statusMap[result.toString()]!!)
            }

        live_data()

        return view
    }

    /**시작시 로그인 상태 확인*/
    override fun onStart() {
        super.onStart()

        //시작시 로직
        viewModel.get_mypage_documents(viewModel.state.value, viewModel.isApproved.value)
    }

    //라이브 데이터
    private fun live_data() {

        //state 상태 변화시 실행되는 라이브 데이터
        viewModel.state.observe(viewLifecycleOwner) {
            viewModel.get_mypage_documents(viewModel.state.value, viewModel.isApproved.value)
        }

        //isApproved 상태 변화시 실행되는 라이브 데이터
        viewModel.isApproved.observe(viewLifecycleOwner) {
            viewModel.get_mypage_documents(viewModel.state.value, viewModel.isApproved.value)
        }

        //서버에서 데이터 받아오면 뷰에 적용하는 라이브 데이터
        viewModel.approval_all_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = ApprovalPaperListRVAdapter(it)
            binding.rvMypageDocument.adapter = dataRVAdapter
            binding.rvMypageDocument.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object :
                ApprovalPaperListRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    //결재 서류 아이디를 통해 상세보기로 이동
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.documentId.toString())
                    startActivity(intent)
                }
            })
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