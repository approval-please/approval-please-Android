package com.umc.approval.ui.fragment.community

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amazonaws.regions.Regions
import com.umc.approval.API
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.ActivityUploadLinkDialogBinding
import com.umc.approval.databinding.ActivityUploadTagDialogBinding
import com.umc.approval.databinding.FragmentCommunityUploadReportBinding
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.ui.activity.CommunityUploadDocumentListActivity
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.upload_activity.ImageUploadAdapter
import com.umc.approval.ui.viewmodel.community.CommunityUploadViewModel
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.util.CrawlingTask
import com.umc.approval.util.S3Util
import com.umc.approval.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class CommunityUploadReportFragment : Fragment() {

    private lateinit var binding: FragmentCommunityUploadReportBinding

    private val viewModel by activityViewModels<CommunityUploadViewModel>()

    /**Image Adapter*/
    private lateinit var imageRVAdapter : ImageUploadAdapter

    /**Open graph Manger*/
    private lateinit var manager: InputMethodManager

    /*링크 다이얼로그*/
    private lateinit var linkDialogBinding : ActivityUploadLinkDialogBinding
    private lateinit var linkButton : ImageButton
    private lateinit var linkEraseButton : ImageButton

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button
    private lateinit var linkDialogEditText : EditText

    /*오픈 그래프 버튼*/
    private lateinit var opengraphText : TextView
    private lateinit var opengraphUrl : TextView
    private lateinit var opengraphImage : ImageView
    private lateinit var opengraphId : ConstraintLayout

    /*태그 다이얼로그*/
    private lateinit var tagDialogBinding : ActivityUploadTagDialogBinding
    private lateinit var tagButton : ImageButton
    private lateinit var tagTextView : TextView
    private lateinit var tagDialogEditText :EditText

    /*태그 데이터*/
    private lateinit var tagString : String
    private lateinit var tagArray : List<String>

    val voteList : ArrayList<String> = arrayListOf()
    val linkList : ArrayList<OpenGraphDto> = ArrayList()

    lateinit var communityUploadActivity: CommunityUploadActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityUploadActivity = context as CommunityUploadActivity
        //communityUploadActivity.changeTitle("결재서류 작성하기")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunityUploadReportBinding.inflate(layoutInflater)
        binding.documentBtn.setOnClickListener{
            Log.d("결재서류", "결재서류 선택 버튼")
        }

        /*Open Graph manager 초기화*/
        manager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager

        //opengraph 초기화
        binding.imageRv.isVisible = false
        binding.openGraphLayout.isVisible = false
        binding.uploadHashtagItem.isVisible = false

        /*이미지 선택시 실행되는 메서드*/
        observe_pic()

        /*이미지 선택 이벤트*/
        image_upload_event()

        /*link observer*/
        link_observe()

        /*opengraph observer*/
        opengraph_observe()

        /*링크 첨부 다이얼로그*/
        binding.uploadLinkBtn.setOnClickListener{
            showLinkDialog()
        }

        /*태그 입력 다이얼로그 열기*/
        tagString = ""
        binding.uploadTagBtn.setOnClickListener{
            showTagDialog()
        }

        binding.documentBtn.setOnClickListener{
            // 클릭 이벤트 처리
            val intent = Intent(requireContext(), CommunityUploadDocumentListActivity::class.java)
            startActivityForResult(intent, 2000)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.setLink(1)
    }

    lateinit var imageUrl : String

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
        linkEraseButton = linkDialogBinding.uploadLinkEraseBtn
        opengraphText = linkDialogBinding.openGraphText
        opengraphUrl = linkDialogBinding.openGraphUrl
        opengraphImage = linkDialogBinding.openGraphImage
        opengraphId = linkDialogBinding.openGraph

        //Dialog Opengraph 초기화
        opengraphId.isVisible = false


        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialogEditText.setText("") //초기화
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            // tagTextView.setText(tagDialogEditText.text.toString())
            binding.openGraphLayout.isVisible = true

            if(opengraphId.isVisible){

                var openGraphDto : OpenGraphDto = OpenGraphDto()
                openGraphDto.url = opengraphUrl.text.toString()
                openGraphDto.title = opengraphText.text.toString()
                openGraphDto.image = imageUrl

                Log.d("dd", openGraphDto.toString())

                if(linkList.size < 4) {
                    linkList.apply {
                        add(openGraphDto)
                    }
                    var imageCount = "(" + linkList.size + "/4)"
                    binding.imageLinkTv.setText(imageCount)
                }
                val dataRVAdapter = CommunityUploadLinkItemRVAdapter(linkList)
                binding.uploadLinkItem.adapter = dataRVAdapter
                binding.uploadLinkItem.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }

            linkDialog.dismiss()
        }


        linkEraseButton.setOnClickListener{
            linkDialogEditText.setText("")
        }

        /*link url 바뀔때 마다 적용*/
        editLinkUrl()

        /*link 팝업*/
        linkDialog.show()
    }


    /**image upload event*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun image_upload_event() {
        binding.uploadImageBtn.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> showGallery()

                // 갤러리 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                -> showPermissionContextPopup()

                // 권한 요청 하기
                else -> requireActivity().requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
        }
    }

    /**Image observer*/
    private fun observe_pic() {
        viewModel.pic.observe(viewLifecycleOwner) {
            binding.imageRv.isVisible = true
            imageRVAdapter = ImageUploadAdapter(viewModel.pic.value!!)
            binding.imageRv.adapter = imageRVAdapter
            binding.imageRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**move to gallery*/
    private fun showGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        this.startActivityForResult(intent, Utils.PICK_IMAGE_FROM_GALLERY)
    }

    /**권한이 없을때 권한 등록 팝업 함수*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한이 필요합니다.")
            .setMessage("갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requireActivity().requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
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
                        Toast.makeText(requireContext(), "사진은 4장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
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
        }else if (requestCode == 2000) {
            if (resultCode == RESULT_OK) {
                val resultMsg = data?.getStringExtra("title")
                binding.documentBtn.text = resultMsg

            } else if (resultCode == RESULT_CANCELED) {
                val resultMsg = data?.getStringExtra("title")
            } else {
            }
        }
    }

    /**권한 요청 승인 이후 실행되는 함수*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.requireActivity().onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showGallery()
                else
                    Toast.makeText(requireContext(), "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * S3
     * */
    /*S3 connect*/
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
                    requireContext(),
                    "approval-please/talk", file, "test"
                )
        }
    }

    /*File Uri for S3 connect*/
    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }

    /**
     * Open Graph
     * */
    /*url 형식 메서드*/
    private fun getUrl(url: String) : String {
        return if(url.contains("http://") || url.contains("https://")) url
        else "https://".plus(url)
    }

    /*link 변경 메서드*/
    private fun editLinkUrl() {
        //addTextChangedListener는 editText속성을 가지는데 값이 변할때마다 viewModel로 결과가 전달
        linkDialogEditText.addTextChangedListener { text: Editable? ->
            text?.let {
                var url = it.toString()
                viewModel.setLink(getUrl(url.trim()))
            }
        }
    }

    /**link live data 변경 시*/
    private fun link_observe() {
        viewModel.link.observe(viewLifecycleOwner) {

            //link 변경시 opengraph 초기화
            opengraphId.isVisible = false

            manager.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            var openGraphDto = OpenGraphDto("", "", "")

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
                            "og:title" -> {
                                el.attr("content")?.let { content ->
                                    openGraphDto.title = content
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
                if (openGraphDto.title.toString() != "") {
                    viewModel.setOpengraph(openGraphDto)
                }
            }
        }
    }

    /**observe graph live data 변경 시*/
    private fun opengraph_observe() {
        viewModel.opengraph.observe(viewLifecycleOwner) {
            opengraphId.isVisible = true
            opengraphText.setText(it.title)
            opengraphUrl.setText(it.url)
            opengraphImage.load(it.image)
            imageUrl = it.image.toString()

        }
    }

    /*태그 다이얼로그*/
    private fun showTagDialog(){
        val tagDialog = Dialog(communityUploadActivity);
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
            // tagTextView.setText(tagDialogEditText.text.toString())
            tagString = tagDialogEditText.text.toString()
            binding.uploadHashtagItem.isVisible = true
            if(tagString.length>1){
                tagArray = tagString.split(" ")

//                tagTextView.setText("("+tagArray.size+"/4)");
                var tagCount = "(" + tagArray.size + "/4)"
                binding.imageTagTv.text = tagCount

                val dataRVAdapter = UploadHashtagRVAdapter(tagArray)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            }

            tagDialog.dismiss()
        }


        /*태그 입력 동적코드*/
//        tagDialogEditText.setOnClickListener(View.OnClickListener {
//            if(tagDialogEditText.text.toString().length==0){
//                tagDialogEditText.setText("#");
//                tagDialogEditText.setSelection(tagDialogEditText.text.length)
//            }
//        })

        //val spannableStringBuilder = SpannableStringBuilder(text)
        tagDialogEditText.setText(tagString)

        var originText = ""
        var hashtagCount = 0;

//        tagDialogEditText.addTextChangedListener(object:TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                originText = s.toString();
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                var text = s.toString()
//                var textLength = text.length-1;
//                if(hashtagCount >= 4){
//                    tagDialogEditText.setText(originText)
//                }
//
//                if(text[textLength] == ' '){
//                    val timer = Timer()
////                    timer.schedule(object : TimerTask() {
////                        override fun run() {
////                            runOnUiThread {
//                                hashtagCount = 0;
//                                for(i in 0..textLength){
//                                    if(text[i] == ' '){
//                                        hashtagCount++;
//                                        val spannableStringBuilder = SpannableStringBuilder(s?.toString() ?: "")
//                                        spannableStringBuilder.setSpan(
//                                            ForegroundColorSpan(Color.parseColor("#6C39FF")),
//                                            //                            BackgroundColorSpan(Color.parseColor("#CBB9FF")),
//                                            0,
//                                            i,
//                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                        )
//                                        if(hashtagCount <= 4){
//                                            tagDialogEditText.setText(spannableStringBuilder.append('#'))
//                                            tagDialogEditText.setSelection(tagDialogEditText.text.length)
//                                        }
//                                    }
//                                }
////                            }
////                        }
////                    }, 10)
//                }
//            }
//        })
        tagDialog.show()
    }

 }