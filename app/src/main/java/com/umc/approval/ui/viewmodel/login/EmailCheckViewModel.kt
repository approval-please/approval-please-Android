package com.umc.approval.ui.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.login.get.EmailCheckDto
import com.umc.approval.dataStore.AccessTokenDataStore
import com.umc.approval.data.repository.login.LoginFragmentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Email Check ViewModel
 * */
class EmailCheckViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()
}