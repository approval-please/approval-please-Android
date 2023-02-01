package com.umc.approval.ui.fragment.community

import android.Manifest
import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.*
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadVoteItemRVAdapter
import com.umc.approval.ui.adapter.upload_activity.ImageUploadAdapter
import com.umc.approval.ui.viewmodel.community.CommunityUploadViewModel
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.ui.viewmodel.community.CommunityViewModel
import com.umc.approval.util.CrawlingTask
import com.umc.approval.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CommunityUploadTokFragment : Fragment() {

    private lateinit var binding: FragmentCommunityUploadTalkBinding

    private val viewModel by activityViewModels<CommunityUploadViewModel>()

    private val commonViewModel by activityViewModels<CommunityViewModel>()

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
        //communityUploadActivity.changeTitle("게시물 작성하기")
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
        binding = FragmentCommunityUploadTalkBinding.inflate(layoutInflater)

        /*Open Graph manager 초기화*/
        manager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager

        //opengraph 초기화
        binding.imageRv.isVisible = false
        binding.openGraphLayout.isVisible = false
        binding.uploadHashtagItem.isVisible = false

        /*이미지 선택시 실행되는 메서드*/
        observe_pic()

        editContent()

        /*이미지 선택 이벤트*/
        image_upload_event()

        /*link observer*/
        link_observe()

        /*opengraph observer*/
        opengraph_observe()

        /*부서명 선택 Spinner*/
        /*서버연동 : 부서명 카테고리 받아서 departments 수정*/
        select_category()

        /*링크 첨부 다이얼로그*/
        binding.uploadLinkBtn.setOnClickListener{
            showLinkDialog();
        }

        //리스트에 값이 변경될 때마다 rv 실행
        viewModel.opengraph_list.observe(viewLifecycleOwner) {
            Log.d("test", it.toString())
        }

        binding.uploadImageBtn

        //vote 초기화
        initVote()
        setVoteList()

        /*태그 입력 다이얼로그 열기*/
        tagString = ""
        binding.uploadTagBtn.setOnClickListener{
            showTagDialog()
        }
        commonViewModel.setLink(0)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        commonViewModel.setLink(0)
    }


    private fun initVote() {
        voteList.apply{
        add("")
        add("")
        }

        binding.voteLayout.isVisible = false

        binding.uploadVoteBtn.setOnClickListener{
            binding.uploadVoteTv.text = "(1/1)"
            binding.voteLayout.isVisible = true
        }

        binding.voteCancelButton.setOnClickListener{
            binding.uploadVoteTv.text = "(0/1)"
            voteList.clear()
            binding.voteLayout.isVisible = false
        }

        binding.voteTitleEraseBtn.setOnClickListener{
            binding.voteTitleEt.setText("")
        }
    }

    // vote rv
    private fun setVoteList(){

        binding.voteItemAddButton.setOnClickListener{
            if(voteList.size <4){
                voteList.apply {
                    add("")
                }
                binding.voteItemCountTv.text = voteList.size.toString()
                val dataRVAdapter = CommunityUploadVoteItemRVAdapter(voteList)
                binding.voteItem.adapter = dataRVAdapter
                binding.voteItem.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
        }
        val dataRVAdapter = CommunityUploadVoteItemRVAdapter(voteList)
        binding.voteItem.adapter = dataRVAdapter
        binding.voteItem.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    // 스피너 높이 조정 함수
    private fun dipToPixels(dipValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dipValue,
            resources.displayMetrics
        )
    }

    fun Spinner.avoidDropdownFocus() {
        val listPopup = Spinner::class.java
            .getDeclaredField("mPopup")
            .apply { isAccessible = true }
            .get(this)
        if (listPopup is ListPopupWindow) {
            val popup = ListPopupWindow::class.java
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(listPopup)
            if (popup is PopupWindow) {
                popup.isFocusable = false
                popup.height = 100
            }
        }
    }

    /**category spinner*/
    private fun select_category() {
        var departments = arrayOf(
            "디지털기기",
            "생활가전",
            "생활용품",
            "가구/인테리어",
            "주방/건강",
            "출산/유아동",
            "패션의류/잡화",
            "뷰티/미용",
            "스포츠/레저/헬스",
            "취미/게임/완구",
            "문구/오피스",
            "도서/음악",
            "티켓/교환권",
            "식품",
            "동물/식물",
            "영화/공연",
            "자동차/공구",
            "기타물품",
        )

        val adapter = object : ArrayAdapter<String>(communityUploadActivity, com.umc.approval.R.layout.item_upload_spinner) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)

                if (position == count) {
                    //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (v.findViewById<View>(com.umc.approval.R.id.tvItemSpinner) as TextView).text = ""
                    //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (v.findViewById<View>(com.umc.approval.R.id.tvItemSpinner) as TextView).hint = getItem(count)
                }

                return v
            }

            override fun getCount(): Int {
                //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }

        adapter.addAll(departments.toMutableList())

        adapter.add("부서를 선택해주세요.")
        binding.uploadDepartmentSpinner.avoidDropdownFocus()
        binding.uploadDepartmentSpinner.adapter = adapter
        binding.uploadDepartmentSpinner.setSelection(adapter.count)
        binding.uploadDepartmentSpinner.dropDownVerticalOffset = dipToPixels(40f).toInt()
        binding.uploadDepartmentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setCategory(position)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.setCategory(18)
                }
            }
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
                    }else{
                        binding.uploadImageTv.text = "("+count.toString()+"/4)"
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
                                            val spannableStringBuilder = SpannableStringBuilder(s?.toString() ?: "")
                                            spannableStringBuilder.setSpan(
                                                ForegroundColorSpan(Color.parseColor("#6C39FF")),
                                                //                            BackgroundColorSpan(Color.parseColor("#CBB9FF")),
                                                0,
                                                i,
                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                            )
                                            tagDialogEditText.text = spannableStringBuilder
                                            tagDialogEditText.setSelection(tagDialogEditText.text.length)

                                        }else if(hashtagCount >= 4 && s.toString()[i]==' '){
                                            tagDialogEditText.setText(originText)
                                            Toast.makeText(requireContext(), "태그는 4개까지 입력가능합니다.", Toast.LENGTH_SHORT).show()
                                            val spannableStringBuilder = SpannableStringBuilder(originText?.toString() ?: "")
                                            spannableStringBuilder.setSpan(
                                                ForegroundColorSpan(Color.parseColor("#6C39FF")),
                                                0,
                                                originText.length-1,
                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                            )
                                            tagDialogEditText.text = spannableStringBuilder
                                            tagDialogEditText.setSelection(s.toString().length-1)
                                        }
                                    }
                                }
                            }
                        }
                    }, 1)

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