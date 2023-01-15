package com.umc.approval.ui.aran

import android.R
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.umc.approval.databinding.ActivityUploadBinding
import com.umc.approval.databinding.ActivityUploadLinkDialogBinding
import com.umc.approval.databinding.ActivityUploadTagDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.DoubleStream.concat


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    /*이미지 불러오기*/
    private lateinit var imageButton: ImageButton

    /*태그 다이얼로그*/
    private lateinit var tagDialogBinding : ActivityUploadTagDialogBinding
    private lateinit var tagButton : ImageButton
    private lateinit var tagTextView : TextView

    /*링크 다이얼로그*/
    private lateinit var linkDialogBinding : ActivityUploadLinkDialogBinding
    private lateinit var linkButton : ImageButton
    private lateinit var linkTextView : TextView
    private lateinit var linkEraseButton : ImageButton
    private lateinit var uploadLinkTitle : TextView

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button
    private lateinit var tagDialogEditText :EditText
    private lateinit var linkDialogEditText :EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*부서명 선택 Spinner*/
        /*서버연동 : 부서명 카테고리 받아서 departments 수정*/
        var departments = arrayOf("부서를 선택해주세요", "디지털 기기", "생활 가전", "생활 용품", "가구/인테리어")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_spinner_item, departments)

        binding.uploadDepartmentSpinner.adapter = adapter
        binding.uploadDepartmentSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        /*이미지 선택*/
        imageButton = binding.uploadImageBtn
        imageButton.setOnClickListener{

        }

        /*태그 입력 다이얼로그 열기*/
        tagButton = binding.uploadTagBtn
        tagButton.setOnClickListener{
            showTagDialog();
        }

        /*링크 첨부 다이얼로그*/
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            showLinkDialog();
        }
        /*cancel 버튼 클릭 이벤트*/

        /*제출 버튼 클릭 이벤트*/
    }

    /*태그 다이얼로그*/
    private fun showTagDialog(){
        val tagDialog = Dialog(this);
        tagDialogBinding = ActivityUploadTagDialogBinding.inflate(layoutInflater)

        tagDialog.setContentView(tagDialogBinding.root)
        tagDialog.setCanceledOnTouchOutside(true)
        tagDialog.setCancelable(true)
        dialogCancelButton = tagDialogBinding.uploadTagDialogCancelButton
        dialogConfirmButton = tagDialogBinding.uploadTagDialogConfirmButton
        tagDialogEditText = tagDialogBinding.uploadTagDialogEt

        /*취소버튼*/
        dialogCancelButton.setOnClickListener{
            tagDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            tagTextView = binding.uploadTagTv;
            tagTextView.setText(tagDialogEditText.text.toString());
            tagDialog.dismiss()
        }

        /*태그 입력 동적코드*/
        //val spannableStringBuilder = SpannableStringBuilder(text)
        tagDialogEditText.setOnClickListener() {
            if(tagDialogEditText.text.toString()==""){
                tagDialogEditText.append("#")
            }
        }

        tagDialogEditText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var text = s.toString()
                var textLength = text.length;
                if(textLength>1&&text[textLength-1] == ' '){
                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                val spannableStringBuilder = SpannableStringBuilder(s?.toString() ?: "")
                                spannableStringBuilder.setSpan(
                                    ForegroundColorSpan(Color.parseColor("#6C39FF")),
                                    //BackgroundColorSpan(Color.parseColor("#CBB9FF")),
                                    0,
                                    s.toString().length-1,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                tagDialogEditText.setText(spannableStringBuilder.append("#"))
                                tagDialogEditText.setSelection(tagDialogEditText.text.length)
                            }
                        }
                    }, 10)
                }
            }
        })
        tagDialog.show()
    }

    /*링크 첨부 다이얼로그*/
    private fun showLinkDialog() {
        val linkDialog = Dialog(this);
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
            linkTextView = binding.uploadLinkTv
            linkTextView.setText(linkDialogEditText.text.toString());
            linkDialog.dismiss()
        }

        linkEraseButton = linkDialogBinding.uploadLinkEraseBtn
        linkEraseButton.setOnClickListener{
            linkDialogEditText.setText("")
        }
        uploadLinkTitle = linkDialogBinding.uploadLinkTitle
        /*url 바뀔때 마다*/
        linkDialogEditText.addTextChangedListener(object:TextWatcher{
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

