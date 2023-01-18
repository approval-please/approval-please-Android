package com.umc.approval.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import java.io.File


class S3Util {


    private var accessKey = "" // IAM AccessKey

    private var secretKey = "" // IAM SecretKey

    private var region // S3 Region
            : Region? = null

    /**
     * 생성자 생성 시 초기 Region 설정 : AP_NORTHEAST_2
     */
    fun S3Util() {
        region = Region.getRegion(Regions.AP_NORTHEAST_2)
    }

    /**
     * S3 파일 업로드
     *
     * @param context    Context
     * @param bucketName S3 버킷 이름(/(슬래쉬) 없이)
     * @param folder     버킷 내 폴더 경로(/(슬래쉬) 맨 앞, 맨 뒤 없이)
     * @param fileName   파일 이름
     * @param file       Local 파일 경로
     * @param listener   AWS S3 TransferListener
     */
    fun uploadWithTransferUtility(
        context: Context?,
        bucketName: String,
        file: File, fileName: String?,
    ) {
        require(!(TextUtils.isEmpty(accessKey) || TextUtils.isEmpty(secretKey))) { "AccessKey & SecretKey must be not null" }
        val awsCredentials: AWSCredentials = BasicAWSCredentials(
            accessKey, secretKey
        )
        val s3Client = AmazonS3Client(
            awsCredentials, region
        )
        val transferUtility = TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()
        TransferNetworkLossHandler.getInstance(context)
        val uploadObserver = transferUtility.upload(
            bucketName, fileName, file
        )
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("success", "success")
                    // Handle a completed upload
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })
    }

    /**
     * Access, Secret Key 설정
     */
    fun setKeys(accessKey: String, secretKey: String): S3Util? {
        this.accessKey = accessKey
        this.secretKey = secretKey
        return this
    }

    /**
     * Region Enum 으로 Region 설정
     */
    fun setRegion(regionName: Regions?): S3Util? {
        region = Region.getRegion(regionName)
        return this
    }

    /**
     * Singleton Pattern
     */
    fun getInstance(): S3Util? {
        return LHolder.instance
    }

    private object LHolder {
        val instance = S3Util()
    }
}