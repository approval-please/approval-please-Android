package com.umc.approval.ui.fragment.community

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amazonaws.regions.Regions
import com.umc.approval.API
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.*
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.ui.activity.CommunityUploadDocumentListActivity
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadVoteItemRVAdapter
import com.umc.approval.ui.adapter.upload_activity.ImageUploadAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.ui.viewmodel.approval.DocumentViewModel
import com.umc.approval.ui.viewmodel.community.CommunityReportUploadViewModel
import com.umc.approval.ui.viewmodel.community.CommunityViewModel
import com.umc.approval.util.BlackToast
import com.umc.approval.util.CrawlingTask
import com.umc.approval.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class CommunityUploadReportFragment : Fragment() {

    private lateinit var binding: FragmentCommunityUploadReportBinding

    private val viewModel by activityViewModels<CommunityReportUploadViewModel>()

    private val commonViewModel by activityViewModels<CommunityViewModel>()

    private val documentViewModel by viewModels<DocumentViewModel>()


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

        /*Open Graph manager 초기화*/
        manager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager

        //opengraph 초기화
        binding.imageRv.isVisible = false
        binding.openGraphLayout.isVisible = false
        binding.uploadHashtagItem.isVisible = false

        viewModel.documentId.observe(viewLifecycleOwner) {
            documentViewModel.get_document_detail(it.toString())
        }

        documentViewModel.document.observe(viewLifecycleOwner) {
            binding.documentBtn.text = it.title.toString()
        }

        /*이미지 선택시 실행되는 메서드*/
        observe_pic()

        editContent()

        /*이미지 선택 이벤트*/
        image_upload_event()

        /*link observer*/
        link_observe()

        /*opengraph observer*/
        opengraph_observe()

        init_link_tag()

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

        commonViewModel.setLink(1)

        return binding.root
    }

    private fun init_link_tag() {
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

        val tagDialog = Dialog(requireContext());
        tagDialogBinding = ActivityUploadTagDialogBinding.inflate(layoutInflater)

        tagDialog.setContentView(tagDialogBinding.root)
        tagDialog.setCanceledOnTouchOutside(true)
        tagDialog.setCancelable(true)
        dialogCancelButton = tagDialogBinding.uploadTagDialogCancelButton
        dialogConfirmButton = tagDialogBinding.uploadTagDialogConfirmButton
        tagDialogEditText = tagDialogBinding.uploadTagDialogEt
    }

    override fun onResume() {
        super.onResume()
        commonViewModel.setLink(1)
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

            if (viewModel.opengraph != null) {
                viewModel.setOpengraph_list(viewModel.opengraph.value!!)
            }
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

            val images = mutableListOf<String>()
            for (i in it) {
                val random = UUID.randomUUID().toString()
                images.add("https://approval-please.s3.ap-northeast-2.amazonaws.com/" + random)
            }
            viewModel.setRealImage(images)
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
                        BlackToast.createToast(requireContext(), "사진은 4장까지 선택 가능합니다.").show()
                        return
                    }else{
                        binding.uploadImageTv.text = "("+count.toString()+"/4)"
                    }
                    for (i in 0 until count) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        list.add(imageUri)
                        viewModel.setImage(list)
                    }
                } else {      // 1장 선택한 경우
                    binding.uploadImageTv.text = "(1/4)"
                    val imageUri = it.data!!
                    list.add(imageUri)
                    viewModel.setImage(list)
                }
            }
        }else if (requestCode == 2000) {
            if (resultCode == RESULT_OK) {
                val resultMsg = data?.getIntExtra("documentId", 100)
                Log.d("test", resultMsg.toString())
                viewModel.setDocumentId(resultMsg!!.toInt())
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
                    BlackToast.createToast(requireContext(), "권한을 거부하셨습니다.").show()
            }
        }
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
                    if (openGraphDto.url.toString() == "") {
                        openGraphDto.url = viewModel.link.value.toString()
                        viewModel.setOpengraph(openGraphDto)
                    } else {
                        viewModel.setOpengraph(openGraphDto)
                    }
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
        val tagDialog = Dialog(requireContext());
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

                val new = mutableListOf<String>()

                for (i in tagArray) {
                    if (i != "") {
                        new.add(i)
                    }
                }

                binding.imageTagTv.text = "("+new.size+"/4)";

                val dataRVAdapter = UploadHashtagRVAdapter(new)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

                viewModel.setTags(new)
            } else {
                binding.imageTagTv.text = "("+0+"/4)";
                val dataRVAdapter = UploadHashtagRVAdapter(listOf())
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

                viewModel.setTags(listOf())
            }
            tagDialog.dismiss()
        }


        /*태그 입력 동적코드*/
        tagDialogEditText.setOnClickListener(View.OnClickListener {
            tagDialogEditText.setSelection(tagDialogEditText.text.length)
        })

        //val spannableStringBuilder = SpannableStringBuilder(text)
        tagDialogEditText.isClickable = false;

        tagDialogEditText.setText(tagString)
        tagDialogEditText.addTextChangedListener(object: TextWatcher {
            var originText = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                tagDialogEditText.setSelection(s.toString().length)
                if(s.toString().isNotEmpty() && s.toString()[s.toString().length -1]==' ')  return
                originText = s.toString();
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() == originText) return

                var text = s.toString()
                var textLength = text.length-1;

                if(tagDialogEditText.isFocusable){
                    var hashtagCount = 0

                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            activity?.runOnUiThread {
                                for(i in 1..textLength){
                                    if(text[i] == ' ' && text[i-1] !=' '){
                                        hashtagCount++;
                                    }
                                    if(i==textLength){

                                        if(hashtagCount < 4){
//                                            val spannableStringBuilder = SpannableStringBuilder(s?.toString() ?: "")
//                                            spannableStringBuilder.setSpan(
//                                                ForegroundColorSpan(Color.parseColor("#6C39FF")),
//                                                //                            BackgroundColorSpan(Color.parseColor("#CBB9FF")),
//                                                0,
//                                                i,
//                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                            )
//                                            tagDialogEditText.text = spannableStringBuilder
//                                            tagDialogEditText.setSelection(tagDialogEditText.text.length)

                                        }else if(hashtagCount >= 4 && s.toString()[i]==' '){
                                            BlackToast.createToast(requireContext(), "태그는 4개까지 입력가능합니다.").show()
                                            tagDialogEditText.setText(originText)
//                                            val spannableStringBuilder = SpannableStringBuilder(originText?.toString() ?: "")
//                                            spannableStringBuilder.setSpan(
//                                                ForegroundColorSpan(Color.parseColor("#6C39FF")),
//                                                0,
//                                                originText.length-1,
//                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                            )
//                                            tagDialogEditText.text = spannableStringBuilder
//                                            tagDialogEditText.setSelection(s.toString().length-1)
                                        }
                                    }
                                }
                            }
                        }
                    }, 0)

                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        tagDialog.show()
    }

    //콘텐츠 변경 메소드
    private fun editContent() {
        //addTextChangedListener는 editText속성을 가지는데 값이 변할때마다 viewModel로 결과가 전달
        binding.uploadContentEt.addTextChangedListener { text: Editable? ->
            text?.let {
                var content = it.toString()
                viewModel.setContent(content.trim())
            }
        }
    }
 }
