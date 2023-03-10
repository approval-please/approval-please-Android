package com.umc.approval.ui.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.amazonaws.regions.Regions
import com.umc.approval.API
import com.umc.approval.R
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import com.umc.approval.databinding.ActivityUploadBinding
import com.umc.approval.databinding.ActivityUploadLinkDialogBinding
import com.umc.approval.databinding.ActivityUploadTagDialogBinding
import com.umc.approval.ui.adapter.upload_activity.ImageUploadAdapter
import com.umc.approval.ui.viewmodel.approval.UploadDocumentViewModel
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.util.BlackToast
import com.umc.approval.util.CrawlingTask
import com.umc.approval.util.S3Util
import com.umc.approval.util.Utils
import com.umc.approval.util.Utils.categoryMapReverse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    /**????????? ?????? ?????????*/
    private lateinit var uploadFile: ApprovalUploadDto

    /**Upload Viewmodel*/
    lateinit var viewModel: UploadDocumentViewModel

    /**Image Adapter*/
    private lateinit var imageRVAdapter : ImageUploadAdapter

    /**Open graph Manger*/
    private lateinit var manager: InputMethodManager

    /*?????? ???????????????*/
    private lateinit var tagDialogBinding : ActivityUploadTagDialogBinding
    private lateinit var tagButton : ImageButton
    private lateinit var tagTextView : TextView
    /*?????? ?????????*/
    private lateinit var tagString : String
    private lateinit var tagArray : List<String>

    /*?????? ?????????*/
    private lateinit var linkString : String

    /*?????? ???????????????*/
    private lateinit var linkDialogBinding : ActivityUploadLinkDialogBinding
    private lateinit var linkButton : ImageButton
    private lateinit var linkTextView : TextView
    private lateinit var linkEraseButton : ImageButton
    private lateinit var opengraphText : TextView
    private lateinit var opengraphUrl : TextView
    private lateinit var opengraphImage : ImageView
    private lateinit var opengraphId : ConstraintLayout
    private lateinit var linkDialog: Dialog

    /*??????????????? ??????*/
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

        /*View Model ?????????*/
        viewModel = ViewModelProvider(this).get(UploadDocumentViewModel::class.java)

        /*Open Graph manager ?????????*/
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        //opengraph ?????????
        binding.openGraph.isVisible = false

        /*????????? ?????? Spinner*/
        /*???????????? : ????????? ???????????? ????????? departments ??????*/
        select_category()

        /*????????? ????????? ???????????? ?????????*/
        observe_pic()

        /*????????? ?????? ?????????*/
        image_upload_event()

        /*move to approval fragment*/
        back_to_approval()

        /*link observer*/
        link_observe()

        /*opengraph observer*/
        opengraph_observe()

        /*?????? ?????? ?????? ????????? ??? approval fragment ??? ??????*/
        upload_item()

        /**init dialog*/

        /*?????? ?????? ??????????????? ??????*/
        tagButton = binding.uploadTagBtn
        tagString = ""
        tagButton.setOnClickListener{
            showTagDialog()
        }

        /*?????? ?????? ???????????????*/
        linkString = ""
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            showLinkDialog()
        }

        live_data()
    }

    //????????? ?????????
    private fun live_data() {
        //????????? ?????? ??????
        viewModel.accessToken.observe(this) {
            if (!it) {
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        /**AccessToken ???????????? ????????? ???????????? ????????? ??????*/
        viewModel.checkAccessToken()
    }

    //????????? ??????????????? ??????
    private fun upload_item() {
        binding.uploadSubmitBtn.setOnClickListener {

            uploadFile = ApprovalUploadDto(title = binding.uploadTitleEt.text.toString()
                , content = binding.uploadContentEt.text.toString())

            if (viewModel.category.value != 18) {
                uploadFile.category = viewModel.category.value

                CoroutineScope(Dispatchers.IO).launch {
                    //????????? ?????? ??????
                    if (viewModel.opengraph.value != null) {
                        uploadFile.opengraph = viewModel.opengraph.value
                    }

                    //????????? ?????? ??????
                    if (viewModel.pic.value != null) {
                        uploadFile.images = viewModel.images.value
                        S3_connect()
                    }

                    //????????? ?????? ??????
                    if (viewModel.tags.value != null) {
                        uploadFile.tag = viewModel.tags.value
                    }

                    viewModel.post_document(uploadFile)
                }

                Handler(Looper.myLooper()!!).postDelayed({
                    finish()
                }, 700)
            } else {
                BlackToast.createToast(this, "????????? ??????????????????").show()
            }
        }
    }

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
        var departments = categoryMapReverse.keys

        val adapter = object : ArrayAdapter<String>(this, R.layout.item_upload_spinner) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)

                if (position == count) {
                    //????????? ???????????? textView ??? ?????? ????????? ???????????????.
                    (v.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                    //???????????? ????????? ?????? ????????? hint??? ????????? ?????????.
                    (v.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(count)
                }

                return v
            }

            override fun getCount(): Int {
                //????????? ???????????? ?????????????????? ???????????? ????????? getCount??? 1??? ????????????.
                return super.getCount() - 1
            }
        }

        adapter.addAll(departments.toMutableList())

        adapter.add("????????? ??????????????????.")

        val newsSourceSpinner = findViewById<View>(R.id.upload_department_spinner) as Spinner


        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true

        // Get private mPopup member variable and try cast to ListPopupWindow
        val popupWindow = popup[newsSourceSpinner] as ListPopupWindow

        // set popup height
        popupWindow.height = 250

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
                }
            }
    }

    /**move to approval fragment*/
    private fun back_to_approval() {
        binding.backToApproval.setOnClickListener {
            finish()
        }
    }

    /*?????? ???????????????*/
    private fun showTagDialog(){
        val tagDialog = Dialog(this);
        tagDialogBinding = ActivityUploadTagDialogBinding.inflate(layoutInflater)

        tagDialog.setContentView(tagDialogBinding.root)
        tagDialog.setCanceledOnTouchOutside(true)
        tagDialog.setCancelable(true)
        dialogCancelButton = tagDialogBinding.uploadTagDialogCancelButton
        dialogConfirmButton = tagDialogBinding.uploadTagDialogConfirmButton
        tagDialogEditText = tagDialogBinding.uploadTagDialogEt

        /*????????????*/
        dialogCancelButton.setOnClickListener{
            tagDialog.dismiss()
        }


        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            tagTextView = binding.uploadTagTv;
            // tagTextView.setText(tagDialogEditText.text.toString())
            tagString = tagDialogEditText.text.toString()

            if(tagString.length>1){
                tagArray = tagString.split(" ")

                val new = mutableListOf<String>()

                for (i in tagArray) {
                    if (i != "") {
                        new.add(i)
                    }
                }

                viewModel.setTags(new)

                tagTextView.setText("("+new.size+"/4)");

                val dataRVAdapter = UploadHashtagRVAdapter(new)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

            }else{
                tagTextView.setText("????????? ??????????????? (0/4)")

                val dataRVAdapter = UploadHashtagRVAdapter(listOf())
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

                viewModel.setTags(listOf())
            }

            tagDialog.dismiss()
        }


        /*?????? ?????? ????????????*/
        tagDialogEditText.setOnClickListener(View.OnClickListener {
            tagDialogEditText.setSelection(tagDialogEditText.text.length)
        })

        //val spannableStringBuilder = SpannableStringBuilder(text)
        tagDialogEditText.isClickable = false;


        tagDialogEditText.setText(tagString)
        tagDialogEditText.addTextChangedListener(object:TextWatcher{
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

                var hashtagCount = 0

                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            for(i in 1..textLength){
                                if(text[i] == ' ' && text[i-1] !=' '){
                                    hashtagCount++;
                                }
                                if(i==textLength){
                                    if(hashtagCount < 4){
//                                        val spannableStringBuilder = SpannableStringBuilder(s?.toString() ?: "")
//                                        spannableStringBuilder.setSpan(
//                                            ForegroundColorSpan(Color.parseColor("#6C39FF")),
//                                            0,
//                                            i,
//                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                        )
                                        //tagDialogEditText.text = spannableStringBuilder
                                        //tagDialogEditText.setSelection(tagDialogEditText.text.length)

                                    }else if(hashtagCount >= 4 && s.toString()[i]==' '){
                                        BlackToast.createToast(this@UploadActivity, "????????? 4????????? ?????????????????????.").show()
                                        tagDialogEditText.setText(originText)
//                                        val spannableStringBuilder = SpannableStringBuilder(originText?.toString() ?: "")
//                                        spannableStringBuilder.setSpan(
//                                            ForegroundColorSpan(Color.parseColor("#6C39FF")),
//                                            0,
//                                            originText.length-1,
//                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                        )
                                        //tagDialogEditText.text = spannableStringBuilder
//                                        tagDialogEditText.setSelection(s.toString().length-1)
                                    }
                                }
                            }
                        }
                    }
                }, 0)


            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        tagDialog.show()
    }

    /*?????? ?????? ???????????????*/
    private fun showLinkDialog() {
        linkDialog = Dialog(this)
        linkDialogBinding = ActivityUploadLinkDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(linkDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)

        //Dialog ?????????
        dialogCancelButton = linkDialogBinding.uploadLinkDialogCancelButton
        dialogConfirmButton = linkDialogBinding.uploadLinkDialogConfirmButton
        linkDialogEditText = linkDialogBinding.uploadLinkDialogEt
        linkEraseButton = linkDialogBinding.uploadLinkEraseBtn
        opengraphText = linkDialogBinding.openGraphText
        opengraphUrl = linkDialogBinding.openGraphUrl
        opengraphImage = linkDialogBinding.openGraphImage
        opengraphId = linkDialogBinding.openGraph

        //Dialog Opengraph ?????????
        opengraphId.isVisible = false

        linkString = ""

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialogEditText.setText(linkString) //?????????
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener {
            linkString = linkDialogEditText.text.toString()
            linkDialog.dismiss()
        }

        linkEraseButton.setOnClickListener{
            linkString = ""
            linkDialogEditText.setText(linkString)
        }

        /*?????? ?????? ???????????????*/
        linkButton = binding.uploadLinkBtn
        linkButton.setOnClickListener{
            linkDialog.show()
        }

        linkDialogEditText.setText(linkString)

        /*link url ????????? ?????? ??????*/
        editLinkUrl()
    }

    /**observe graph live data ?????? ???*/
    private fun opengraph_observe() {
        viewModel.opengraph.observe(this) {
            opengraphId.isVisible = true
            opengraphText.setText(it.title)
            opengraphUrl.setText(it.url)
            opengraphImage.load(it.image)

            binding.openGraph.isVisible = true
            binding.uploadLinkTv.isVisible = false
            binding.openGraphText.setText(it.title)
            binding.openGraphUrl.setText(it.url)
            binding.openGraphImage.load(it.image)
        }
    }

    /**link live data ?????? ???*/
    private fun link_observe() {
        viewModel.link.observe(this) {

            //link ????????? opengraph ?????????
            opengraphId.isVisible = false
            binding.openGraph.isVisible = false
            binding.uploadLinkTv.isVisible = true

            manager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
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

    /**image upload event*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun image_upload_event() {
        binding.uploadImageBtn.setOnClickListener {
            when {
                // ????????? ?????? ????????? ?????? ??????
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> showGallery(this)

                // ????????? ?????? ????????? ?????? ?????? && ????????? ????????? ???????????? ?????? ??????
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                -> showPermissionContextPopup()

                // ?????? ?????? ??????
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

            val images = mutableListOf<String>()
            for (i in it) {
                val random = UUID.randomUUID().toString()
                images.add("https://approval-please.s3.ap-northeast-2.amazonaws.com/" + random)
            }
            viewModel.setImages(images)
        }
    }

    /**move to gallery*/
    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activity.startActivityForResult(intent, Utils.PICK_IMAGE_FROM_GALLERY)
    }

    /**????????? ????????? ?????? ?????? ?????? ??????*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("????????? ???????????????.")
            .setMessage("????????? ?????? ????????? ???????????????.")
            .setPositiveButton("????????????") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
            .setNegativeButton("????????????") { _, _ -> }
            .create()
            .show()
    }

    /**?????? ??????(??????????????? ??????) ?????? ???????????? ??????*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            val list = mutableListOf<Uri>()

            data?.let { it ->
                if (it.clipData != null) {   // ????????? ????????? ????????? ??????
                    val count = it.clipData!!.itemCount
                    if (count > 4) {
                        BlackToast.createToast(this, "????????? 4????????? ?????? ???????????????.").show()
                        return
                    }else{
                        binding.tvImageCount.text = "("+count.toString()+"/4)"
                    }
                    for (i in 0 until count) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        list.add(imageUri)
                        viewModel.setImage(list)
                    }
                } else {      // 1??? ????????? ??????
                    val imageUri = it.data!!
                    binding.tvImageCount.text = "(1/4)"
                    list.add(imageUri)
                    viewModel.setImage(list)
                }
            }
        }
    }

    /**?????? ?????? ?????? ?????? ???????????? ??????*/
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
                    BlackToast.createToast(this, "????????? ?????????????????????.").show()
            }
        }
    }

    /**
     * S3
     * */
    /*S3 connect*/
    private fun S3_connect() {

        for ((index,uri) in viewModel.pic.value!!.withIndex()) {

            val new_list = viewModel.images.value!![index].split("/")

            /**uri ??????*/
            val realPathFromURI = getRealPathFromURI(uri)
            val file = File(realPathFromURI)

            Log.d("new_list", new_list.toString())

            /**S3??? ??????*/
            S3Util().getInstance()
                ?.setKeys(API.S3_ACCESS_KEY, API.S3_ACCESS_SECRET_KEY)
                ?.setRegion(Regions.AP_NORTHEAST_2)
                ?.uploadWithTransferUtility(
                    this,
                    "approval-please", file, new_list.get(new_list.size-1)
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
        var cursor = contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }

    /**
     * Open Graph
     * */
    /*url ?????? ?????????*/
    private fun getUrl(url: String) : String {
        return if(url.contains("http://") || url.contains("https://")) url
        else "https://".plus(url)
    }

    /*link ?????? ?????????*/
    private fun editLinkUrl() {
        //addTextChangedListener??? editText????????? ???????????? ?????? ??????????????? viewModel??? ????????? ??????
        linkDialogEditText.addTextChangedListener { text: Editable? ->
            text?.let {
                var url = it.toString()
                viewModel.setLink(getUrl(url.trim()))
            }
        }
    }
}


