package com.umc.approval.check.collie
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.FragmentOtherpageDocumentBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.approval_fragment.ApprovalPaperListRVAdapter
import com.umc.approval.ui.fragment.approval.ApprovalBottomSheetDialogStatusFragment
import com.umc.approval.ui.viewmodel.otherpage.OtherpageDocumentViewModel

/*
 다른 사람 프로필 결재 서류 탭
 */
class OtherpageDocumentFragment(val userId : Int) : Fragment() {

    private var _binding : FragmentOtherpageDocumentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OtherpageDocumentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOtherpageDocumentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.get_other_document(userId, null)
        document_live_data()
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.stateSelect.setOnClickListener {
            val bottomSheetDialog = ApprovalBottomSheetDialogStatusFragment(binding.stateText.text.toString())
            bottomSheetDialog.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.RoundCornerBottomSheetDialogTheme
            )
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }
        childFragmentManager
            .setFragmentResultListener("status", this) { _, bundle ->
                val result = bundle.getString("result")
                binding.stateText.text = result

                // 리사이클러뷰 아이템 갱신
                Log.d("status", result.toString())
                when(result){
                    "상태 전체" -> {
                        viewModel.get_other_document(userId, null)
                        document_live_data()
                    }
                    "승인됨" -> {
                        viewModel.get_other_document(userId, 0)
                        document_live_data()
                    }
                    "반려됨" -> {
                        viewModel.get_other_document(userId, 1)
                        document_live_data()
                    }
                    "결재 대기중" -> {
                        viewModel.get_other_document(userId, 2)
                        document_live_data()
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

    /* 문서 라이브 데이터 받아오는 함수 */
    fun document_live_data(){
        viewModel.document.observe(viewLifecycleOwner){
            val documentRVAdapter = ApprovalPaperListRVAdapter(it)
            binding.rvOtherpageDocument.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.rvOtherpageDocument.adapter = documentRVAdapter
            documentRVAdapter.setOnItemClickListener(object : ApprovalPaperListRVAdapter.OnItemClickListner{
                override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                    //결재 서류 아이디를 통해 상세보기로 이동
                    val intent = Intent(requireContext(), DocumentActivity::class.java)
                    intent.putExtra("documentId", data.documentId.toString())
                    startActivity(intent)
                }
            })
        }
    }
}