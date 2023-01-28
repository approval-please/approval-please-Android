package com.umc.approval.ui.fragment.community

import android.Manifest
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.amazonaws.regions.Regions
import com.umc.approval.API
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.*
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.ui.adapter.upload_activity.ImageUploadAdapter
import com.umc.approval.ui.viewmodel.community.CommunityTokUploadViewModel
import com.umc.approval.ui.viewmodel.community.CommunityUploadViewModel
import com.umc.approval.util.CrawlingTask
import com.umc.approval.util.S3Util
import com.umc.approval.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CommunityUploadTokFragment : Fragment() {

    private lateinit var binding: FragmentCommunityUploadTalkBinding

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
        binding.openGraph.isVisible = false
        binding.imageRv.isVisible = false

        /*이미지 선택시 실행되는 메서드*/
        observe_pic()

        /*이미지 선택 이벤트*/
        image_upload_event()

        /*link observer*/
        link_observe()

        /*opengraph observer*/
        opengraph_observe()

        /*부서명 선택 Spinner*/
        /*서버연동 : 부서명 카테고리 받아서 departments 수정*/
        spinner()

        /*링크 첨부 다이얼로그*/
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            showLinkDialog();
        }

        //리스트에 값이 변경될 때마다 rv 실행
        viewModel.opengraph_list.observe(viewLifecycleOwner) {
            Log.d("test", it.toString())
        }

        binding.uploadImageBtn

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.setLink(0)
    }

    /**category spinner*/
    private fun spinner() {
        var departments = arrayOf("부서 선택", "디지털 기기", "생활 가전", "생활 용품", "가구/인테리어")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(communityUploadActivity, R.layout.simple_spinner_item, departments)

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

        /*검색 삭제*/
        linkEraseButton.setOnClickListener{
            linkDialogEditText.setText("")
        }

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialogEditText.setText("") //초기화
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener {
            linkDialog.dismiss()

            if (viewModel.opengraph != null) {
                viewModel.setOpengraph_list(viewModel.opengraph.value!!)
            }
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
            binding.openGraph.isVisible = false

            manager.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
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

    /**observe graph live data 변경 시*/
    private fun opengraph_observe() {
        viewModel.opengraph.observe(viewLifecycleOwner) {
            opengraphId.isVisible = true
            opengraphText.setText(it.title)
            opengraphUrl.setText(it.url)
            opengraphImage.load(it.image) }
    }
}