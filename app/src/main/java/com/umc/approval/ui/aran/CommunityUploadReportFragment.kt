package com.umc.approval.ui.aran

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.umc.approval.R
import com.umc.approval.databinding.ActivityUploadLinkDialogBinding
import com.umc.approval.databinding.FragmentCommunityUploadReportBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CommunityUploadReportFragment : Fragment() {

    private lateinit var binding: FragmentCommunityUploadReportBinding
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
        //communityUploadActivity.changeTitle("결재서류 작성하기")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunityUploadReportBinding.inflate(layoutInflater)
        binding.documentBtn.setOnClickListener{
            Log.d("결재서류", "결재서류 선택 버튼")
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