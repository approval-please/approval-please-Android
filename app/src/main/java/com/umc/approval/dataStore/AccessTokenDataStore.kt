package com.umc.approval.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.umc.approval.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AccessTokenDataStore {

    /**
     * AppContext 가지고 옴
     * */
    private val context = App.context()

    /**
     * dataStore 객체 전역 변수로 초기화
     * */
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("user_token")
    }

    private val mDataStore : DataStore<Preferences> = context.dataStore

    private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")

    /**
     * ACCESS_TOKEN의 값을 변경하는 함수
     * edit 메소드를 통해 token 값 저장
     * */
    suspend fun setAccessToken(token : String) {
        mDataStore.edit {
            preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    /**
     * 현재 ACCESS_TOKEN 값을 가지고오는 함수
     * catch 메소드를 통해 예외 처리
     * map 메소드를 통해 데이터 가져옴
     * */
    //파일 접근을 위해서는 data 메소드 사용
    //캐치로 예외처리하고 웹 블록 안에서 키를 전달하여 플로우로 반환받으면 됨
    suspend fun getAccessToken(): Flow<String> {
        return mDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[ACCESS_TOKEN] ?: ""
            }
    }

    /**
     * ACCESS_TOKEN을 초기화 해주는 함수
     * 로그아웃시 edit 메소드를 통해 ACCESS_TOKEN 초기화
     * */
    suspend fun deleteAccessToken() {
        mDataStore.edit {
                preferences ->
            preferences[ACCESS_TOKEN] = ""
        }
    }
}