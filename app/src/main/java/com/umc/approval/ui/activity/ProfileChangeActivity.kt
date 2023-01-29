package com.umc.approval.ui.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.amazonaws.regions.Regions
import com.umc.approval.API.S3_ACCESS_KEY
import com.umc.approval.API.S3_ACCESS_SECRET_KEY
import com.umc.approval.data.dto.profile.ProfileChange
import com.umc.approval.databinding.ActivityProfileChangeBinding
import com.umc.approval.ui.viewmodel.profile.ProfileChangeViewModel
import com.umc.approval.util.S3Util
import com.umc.approval.util.Utils.PICK_IMAGE_FROM_GALLERY
import com.umc.approval.util.Utils.PICK_IMAGE_FROM_GALLERY_PERMISSION
import java.io.File

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding
    lateinit var viewModel: ProfileChangeViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ProfileChangeViewModel::class.java)

        binding.saveButton.isVisible = false

        //초기화 데이터
//        viewModel.init_data()

        viewModel.my_profile()

        //다른 view로 이동
        move_to_other()

        //load_profile live data
        load_profile_live_data()

        //live data
        iamge_live_data()

        // 사진첨부 버튼 클릭 이벤트 구현
        pick_photo()

        //profile change event 발생
        save()

        edit()
    }

    /**image introduction nickname live data*/
    private fun iamge_live_data() {
        viewModel.image.observe(this) {
            binding.saveButton.isVisible = true
            binding.profileImage.load(it)
        }
    }

    /***/
    /*link 변경 메서드*/
    private fun edit() {
        binding.nickname.addTextChangedListener { text: Editable? ->
            text?.let {
                binding.saveButton.isVisible = true
            }
        }

        binding.my.addTextChangedListener { text: Editable? ->
            text?.let {
                binding.saveButton.isVisible = true
            }
        }
    }

    /**load_profile_live_data*/
    private fun load_profile_live_data() {
        viewModel.load_profile.observe(this) {
            //닉네임
            binding.nickname.setText(viewModel.load_profile.value!!.nickname)

            //자기소개
            binding.my.setText(viewModel.load_profile.value!!.introduction)

            //profile image
            if (!viewModel.load_profile.value!!.profileImage.equals(null)) {
                binding.profileImage.load(viewModel.load_profile.value!!.profileImage)
            }
        }
    }

    /**저장 로직*/
    private fun save() {
        binding.saveButton.setOnClickListener {

            var profile = ProfileChange()

            if (viewModel.image.value != null) {

                /**uri 변환*/
                val realPathFromURI = getRealPathFromURI(viewModel.image.value!!)
                val file = File(realPathFromURI)

                /**S3에 저장*/
                S3Util().getInstance()
                    ?.setKeys(S3_ACCESS_KEY, S3_ACCESS_SECRET_KEY)
                    ?.setRegion(Regions.AP_NORTHEAST_2)
                    ?.uploadWithTransferUtility(
                        this,
                        "approval-please/profile", file, "my"
                    )

                profile.image = "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/my"
            }

            profile.nickname = binding.nickname.text.toString()
            profile.introduction = binding.my.text.toString()

            viewModel.change_profile(profile)
        }
    }

    /**사진 첨부*/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun pick_photo() {
        binding.profieChange.setOnClickListener {
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
                    PICK_IMAGE_FROM_GALLERY_PERMISSION
                )
            }
        }
    }

    /**다른 뷰로 이동*/
    private fun move_to_other() {
        /**mypage로 이동*/
        binding.backToProfile.setOnClickListener {
            finish()
        }
    }

    /**
     * 갤러리 화면으로 이동
     * */
    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
    }

    /**
     * 권한이 없을때 권한 등록 팝업 함수
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_FROM_GALLERY_PERMISSION)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    /**
     * 사진 선택(갤러리에서 나온) 이후 실행되는 함수
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

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
                        viewModel.setImage(imageUri)
                    }
                } else {      // 1장 선택한 경우
                    val imageUri = it.data!!
                    viewModel.setImage(imageUri)
                }
            }
        }
    }

    /**
     * 권한 요청 승인 이후 실행되는 함수
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PICK_IMAGE_FROM_GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showGallery(this)
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * File Uri path
     * */
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
}