package com.umc.approval.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.umc.approval.databinding.ActivityProfileChangeBinding
import com.umc.approval.ui.viewmodel.profile.ProfileImageViewModel

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding
    lateinit var viewModel: ProfileImageViewModel

    private val PICK_IMAGE_FROM_GALLERY = 1000
    private val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /**mypage로 이동*/
        binding.backToProfile.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(this).get(ProfileImageViewModel::class.java)

        viewModel.profile.observe(this) {
            binding.profileImage.load(it)
            Log.d("Profile_Image", it.toString())
        }

        // 사진첨부 버튼 클릭 이벤트 구현
        binding.profieChange.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> showGallery(this)

                // 갤러리 접근 권한이 없는 경우 && 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> showPermissionContextPopup()

                // 권한 요청 하기
                else -> requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_FROM_GALLERY_PERMISSION)
            }
        }

        //사진 저장 이벤트 구현
        binding.saveButton.setOnClickListener {
        }
    }

    /**
     * 갤러리 화면으로 이동
     * */
    private fun showGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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