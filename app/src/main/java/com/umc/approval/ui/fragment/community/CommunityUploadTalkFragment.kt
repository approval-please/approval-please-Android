package com.umc.approval.ui.fragment.community

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.umc.approval.databinding.*
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.util.OpenGraphParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommunityUploadTalkFragment : Fragment() {

    private lateinit var binding: FragmentCommunityUploadTalkBinding

    /*링크 다이얼로그*/
    private lateinit var linkDialogBinding : ActivityUploadLinkDialogBinding
    private lateinit var linkButton : ImageButton
    private lateinit var linkTextView : TextView
    private lateinit var linkEraseButton : ImageButton
    private lateinit var uploadLinkTitle : TextView

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button
    private lateinit var tagDialogEditText : EditText
    private lateinit var linkDialogEditText : EditText

    lateinit var communityUploadActivity: CommunityUploadActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityUploadActivity = context as CommunityUploadActivity
        //communityUploadActivity.changeTitle("게시물 작성하기")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunityUploadTalkBinding.inflate(layoutInflater)


        /*부서명 선택 Spinner*/
        /*서버연동 : 부서명 카테고리 받아서 departments 수정*/
        var departments = arrayOf("부서 선택", "디지털 기기", "생활 가전", "생활 용품", "가구/인테리어")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(communityUploadActivity, android.R.layout.simple_spinner_item, departments)

        binding.uploadDepartmentSpinner.adapter = adapter
        binding.uploadDepartmentSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        /*링크 첨부 다이얼로그*/
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            showLinkDialog();
        }

        return binding.root
    }
    /*링크 첨부 다이얼로그*/
    private fun showLinkDialog() {
        val linkDialog = Dialog(communityUploadActivity);
        linkDialogBinding = ActivityUploadLinkDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(linkDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = linkDialogBinding.uploadLinkDialogCancelButton
        dialogConfirmButton = linkDialogBinding.uploadLinkDialogConfirmButton
        linkDialogEditText = linkDialogBinding.uploadLinkDialogEt

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener {
            // linkTextView = binding.uploadLinkTv
            // linkTextView.setText(linkDialogEditText.text.toString());
            linkDialog.dismiss()
        }

        linkEraseButton = linkDialogBinding.uploadLinkEraseBtn
        linkEraseButton.setOnClickListener{
            linkDialogEditText.setText("")
        }
        uploadLinkTitle = linkDialogBinding.uploadLinkTitle
        /*url 바뀔때 마다*/
        linkDialogEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var url = s.toString()
                if(url.contains(".")){
                    CoroutineScope(Dispatchers.Default).launch {
                        val ogTags = OpenGraphParser.parse(url)
                        val text = ogTags.toString()

                        CoroutineScope(Dispatchers.Main).launch {
                            uploadLinkTitle.text = text
                        }
                    }
                }

            }
        })

        linkDialog.show()
    }
}