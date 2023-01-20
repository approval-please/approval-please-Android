package com.umc.approval.ui.activity

import android.Manifest
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.amazonaws.regions.Regions
import com.umc.approval.API
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.ActivityUploadBinding
import com.umc.approval.databinding.ActivityUploadLinkDialogBinding
import com.umc.approval.databinding.ActivityUploadTagDialogBinding
import com.umc.approval.ui.adapter.upload_activity.ImageUploadAdapter
import com.umc.approval.ui.viewmodel.upload.UploadViewModel
import com.umc.approval.util.CrawlingTask
import com.umc.approval.util.S3Util
import com.umc.approval.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    /**Upload Viewmodel*/
    lateinit var viewModel: UploadViewModel

    /**Image Adapter*/
    private lateinit var imageRVAdapter : ImageUploadAdapter

    /**Open graph Manger*/
    private lateinit var manager: InputMethodManager

    /*태그 다이얼로그*/
    private lateinit var tagDialogBinding : ActivityUploadTagDialogBinding
    private lateinit var tagButton : ImageButton
    private lateinit var tagTextView : TextView

    /*링크 다이얼로그*/
    private lateinit var linkDialogBinding : ActivityUploadLinkDialogBinding
    private lateinit var linkButton : ImageButton
    private lateinit var linkTextView : TextView
    private lateinit var linkEraseButton : ImageButton
    private lateinit var opengraphText : TextView
    private lateinit var opengraphUrl : TextView
    private lateinit var opengraphImage : ImageView
    private lateinit var opengraphId : ConstraintLayout

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button
    private lateinit var tagDialogEditText :EditText
    private lateinit var linkDialogEditText :EditText


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)

        /*부서명 선택 Spinner*/
        /*서버연동 : 부서명 카테고리 받아서 departments 수정*/
        select_category()

        /*이미지 선택시 실행되는 메서드*/
        observe_pic()

        /*이미지 선택 이벤트*/
        image_upload_event()

        /*move to approval fragment*/
        back_to_approval()

        /*태그 입력 다이얼로그 열기*/
        tagButton = binding.uploadTagBtn
        tagButton.setOnClickListener{
            showTagDialog()
        }

        /*링크 첨부 다이얼로그*/
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            showLinkDialog()
        }

        /*제출 버튼 클릭 이벤트 후 approval fragment 로 이동*/
        binding.uploadSubmitBtn.setOnClickListener {
            S3_connect()
            finish()
        }

        binding.openGraph.isVisible = false

        /*Open Graph manager 초기화*/
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        /*link observer*/
        link_observe()

        /*opengraph observer*/
        opengraph_observe()
    }

    /**category spinner*/
    private fun select_category() {
        var departments = arrayOf("부서를 선택해주세요", "디지털 기기", "생활 가전", "생활 용품", "가구/인테리어")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_spinner_item, departments)

        binding.uploadDepartmentSpinner.adapter = adapter
        binding.uploadDepartmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    /**move to approval fragment*/
    private fun back_to_approval() {
        binding.backToApproval.setOnClickListener {
            finish()
        }
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
            linkDialog.dismiss()
        }

        linkEraseButton = linkDialogBinding.uploadLinkEraseBtn
        linkEraseButton.setOnClickListener{
            linkDialogEditText.setText("")
        }

        opengraphText = linkDialogBinding.openGraphText
        opengraphUrl = linkDialogBinding.openGraphUrl
        opengraphImage = linkDialogBinding.openGraphImage
        opengraphId = linkDialogBinding.openGraph

        opengraphId.isVisible = false

        /*link url 바뀔때 마다 적용*/
        editLinkUrl()

        /*link 팝업*/
        linkDialog.show()
    }

    /**observe graph live data 변경 시*/
    private fun opengraph_observe() {
        viewModel.opengraph.observe(this) {
            opengraphId.isVisible = true
            opengraphText.setText(it.title)
            opengraphUrl.setText(it.url)
            opengraphImage.load(it.image)

            binding.openGraph.isVisible = true
            binding.openGraphText.setText(it.title)
            binding.openGraphUrl.setText(it.url)
            binding.openGraphImage.load(it.image)
        }
    }

    /**link live data 변경 시*/
    private fun link_observe() {
        viewModel.link.observe(this) {
            opengraphId.isVisible = false
            binding.openGraph.isVisible = false
            manager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            var openGraphDto = OpenGraphDto("", "", "", "", "")

            CoroutineScope(Dispatchers.IO).launch {
                val elements = CrawlingTask.getElements(it)
                elements?.let {
                    it.forEach { el ->
                        when (el.attr("property")) {
                            "og:url" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.url = content
                                }
                            }
                            "og:site_name" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.siteName = content
                                }
                            }
                            "og:title" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.title = content
                                }
                            }
                            "og:description" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.description = content
                                }
                            }
                            "og:image" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.image = content
                                }
                            }
                        }
                    }
                }
                if (openGraphDto.title.toString() != "" && openGraphDto.description.toString() != "") {
                    viewModel.setOpengraph(openGraphDto)
                }
            }
        }
    }

    /**image upload event*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun image_upload_event() {
        binding.uploadImageBtn.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> showGallery(this)

                // 갤러리 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                -> showPermissionContextPopup()

                // 권한 요청 하기
                else -> requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
        }
    }

    /**Image observer*/
    private fun observe_pic() {
        viewModel.pic.observe(this) {
            imageRVAdapter = ImageUploadAdapter(viewModel.pic.value!!)
            binding.uploadItem.adapter = imageRVAdapter
            binding.uploadItem.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**move to gallery*/
    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activity.startActivityForResult(intent, Utils.PICK_IMAGE_FROM_GALLERY)
    }

    /**권한이 없을때 권한 등록 팝업 함수*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    /**사진 선택(갤러리에서 나온) 이후 실행되는 함수*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            val list = mutableListOf<Uri>()

            data?.let { it ->
                if (it.clipData != null) {   // 사진을 여러개 선택한 경우
                    val count = it.clipData!!.itemCount
                    if (count > 4) {
                        Toast.makeText(this, "사진은 4장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }
                    for (i in 0 until count) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        list.add(imageUri)
                        viewModel.setImage(list)
                    }
                } else {      // 1장 선택한 경우
                    val imageUri = it.data!!
                    list.add(imageUri)
                    viewModel.setImage(list)
                }
            }
        }
    }

    /**권한 요청 승인 이후 실행되는 함수*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showGallery(this)
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**S3*/
    private fun S3_connect() {
        for (uri in viewModel.pic.value!!) {

            /**uri 변환*/
            val realPathFromURI = getRealPathFromURI(uri)
            val file = File(realPathFromURI)

            /**S3에 저장*/
            S3Util().getInstance()
                ?.setKeys(API.S3_ACCESS_KEY, API.S3_ACCESS_SECRET_KEY)
                ?.setRegion(Regions.AP_NORTHEAST_2)
                ?.uploadWithTransferUtility(
                    this,
                    "approval-please/approval", file, "test"
                )
        }
    }

    /**File Uri path*/
    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }

    /**Open Graph*/
    //url 형식 메서드
    private fun getUrl(url: String) : String {
        return if(url.contains("http://") || url.contains("https://")) url
        else "https://".plus(url)
    }

    //link 변경 메서드
    private fun editLinkUrl() {

        //addTextChangedListener는 editText속성을 가지는데 값이 변할때마다 viewModel로 결과가 전달
        linkDialogEditText.addTextChangedListener { text: Editable? ->
            text?.let {
                var url = it.toString()
                viewModel.setLink(getUrl(url.trim()))
            }
        }
    }
}

