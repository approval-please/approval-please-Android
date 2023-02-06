package com.umc.approval.ui.viewmodel.communityDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.AgreeDto
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.approval.get.InterestingDto
import com.umc.approval.data.dto.approval.get.LikeReturnDto
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.approval.post.TokLikeDto
import com.umc.approval.data.dto.communityReport.get.CommunityReportDetailDto
import com.umc.approval.data.dto.communitydetail.get.CommunityTokDetailDto
import com.umc.approval.data.dto.communitydetail.get.VoteOption
import com.umc.approval.data.dto.communitydetail.post.CommunityVotePost
import com.umc.approval.data.dto.communitydetail.post.CommunityVoteResult
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.data.repository.follow.FollowFragmentRepository
import com.umc.approval.data.repository.like.LikeRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Document 상세 보기 페이지
 * ViewModel 체크 완료
 * */
class TokViewModel() : ViewModel() {

    /**community repository*/
    private val repository = CommunityRepository()

    //서버에서 받아올 톡 데이터
    private var _tok = MutableLiveData<CommunityTokDetailDto>()
    val tok : LiveData<CommunityTokDetailDto>
        get() = _tok

    //엑세스 토큰 체크 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    //엑세스 토큰이 존재할시 True값 지정, True 시 로그인 상태
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    private var _votePeopleEachOption = MutableLiveData<CommunityVoteResult>()
    val votePeopleEachOption : LiveData<CommunityVoteResult>
        get() = _votePeopleEachOption

    //재투표 한 경우
    private var _reVote = MutableLiveData<Boolean>()
    val reVote : LiveData<Boolean>
        get() = _reVote

    //투표를 한 경우
    private var _isVote = MutableLiveData<Boolean>()
    val isVote : LiveData<Boolean>
        get() = _isVote

    //투표를 종료한 경우
    private var _isEnd = MutableLiveData<Boolean>()
    val isEnd : LiveData<Boolean>
        get() = _isEnd

    //투표한 리스트
    private var _votedList = MutableLiveData<MutableList<Int>>()
    val votedList : LiveData<MutableList<Int>>
        get() = _votedList

    //투표 종료 설정
    fun setIsEnd(boolean: Boolean) {
        _isEnd.postValue(boolean)
    }

    //재투표 설정
    fun setReVote(li:Boolean) {
        _reVote.postValue(li)
    }

    //투표했는지 안했는지 설정
    fun setVoted(li:Boolean) {
        _isVote.postValue(li)
    }

    //투표한 곳 초기화
    fun initVoteList() = viewModelScope.launch {
        val list = mutableListOf<Int>()

        //투표지가 없거나 선택되지 투표를 하지 않았을 경우
        if (tok.value!!.voteSelect != null && tok.value!!.voteSelect!!.isNotEmpty()) {

            for (i in tok.value!!.voteSelect!!) {
                list.add(i.voteOptionId)
            }
        }

        if (list.isEmpty()) {
            _isVote.postValue(false)
        } else {
            _isVote.postValue(true)
        }

        _votedList.postValue(list)
    }

    //각 투표 수 초기화
    fun setVotePeopleEachOption(li:CommunityVoteResult) = viewModelScope.launch {
        _votePeopleEachOption.postValue(li)
    }

    /**
     * 테스트용 데이터
     * 정상 동작 Check 완료
     * */
    fun init() = viewModelScope.launch {

        val data = CommunityTokDetailDto(0,
            "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/my", "팀", 2, 2,
            0, "- 안녕하세요 선생님들\n" +
                    "이번에 맥북 M2가 나왔다고 해서 당근에 기존 맥북들이 싸게 올라와있더라구요\n" +
                    "그래서 이번에 맥북 하나 중고로 장만할까 해서 당근 좀 둘러보다가 괜찮은 거 하나 있어서 여쭤봅니다\n" +
                    "금액은 52만원에 올라왔구요\n" +
                    "스펙은 다음과 같습니다\n" +
                    "    \n" +
                    "    제품 : 맥북 프로 2017 터치바 15인치\n" +
                    "    CPU : i7\n" +
                    "    램 : 16기가\n" +
                    "    SSD : 1TB\n" +
                    "    그래픽 : Intel Iris Plus Graphics 650\n" +
                    "    배터리 사이클 : 156회\n" +
                    "    \n" +
                    "    노트북 상태는 사진과 같이 자잘한 찍힘 말고는 전혀 문제 없다고 하네요\n" +
                    "    이 정도 사양에 80만원이면 괜찮은가요? 살지 말지 고민입니다..",
            listOf(
                OpenGraphDto("https://www.daangn.com/articles/525698779",
                    "2017 맥북프로 15인치 고급... | 당근마켓 중고거래","https://dnvefa72aowie.cloudfront.net/origin/article/202301/2CA295C453C84A4C1082AEE102A1CE59F67D4EE3DFD7F072AD9994DBEC968C3E.jpg?q=95&s=1440x1440&t=inside"),
                OpenGraphDto("https://www.daangn.com/articles/525698779",
                    "2017 맥북프로 15인치 고급... | 당근마켓 중고거래","https://dnvefa72aowie.cloudfront.net/origin/article/202301/2CA295C453C84A4C1082AEE102A1CE59F67D4EE3DFD7F072AD9994DBEC968C3E.jpg?q=95&s=1440x1440&t=inside"),
                OpenGraphDto("https://www.daangn.com/articles/525698779",
                    "2017 맥북프로 15인치 고급... | 당근마켓 중고거래","https://dnvefa72aowie.cloudfront.net/origin/article/202301/2CA295C453C84A4C1082AEE102A1CE59F67D4EE3DFD7F072AD9994DBEC968C3E.jpg?q=95&s=1440x1440&t=inside"),
                OpenGraphDto("https://www.daangn.com/articles/525698779",
                    "2017 맥북프로 15인치 고급... | 당근마켓 중고거래","https://dnvefa72aowie.cloudfront.net/origin/article/202301/2CA295C453C84A4C1082AEE102A1CE59F67D4EE3DFD7F072AD9994DBEC968C3E.jpg?q=95&s=1440x1440&t=inside"))
            ,
            listOf("맥북", "노트북"), listOf<String>("https://approval-please.s3.ap-northeast-2.amazonaws.com/approval/docu1-1.png",
                "https://approval-please.s3.ap-northeast-2.amazonaws.com/approval/docu1-2.png",
                "https://approval-please.s3.ap-northeast-2.amazonaws.com/approval/docu1-3.png"),
            3, "어느 것이 좋을까요", false, 96, null, false,
            listOf(VoteOption(1, "A가 좋을까요"), VoteOption(2, "B가 좋을까요")),
            listOf(),
            listOf(32, 64), true, 32, false, false, false, 24, false,
            "50분전", 32, 26, false)

        _tok.postValue(data)
    }

    /**
     * tok을 반환받는 API
     * */
    fun get_tok_detail(tokId : String) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.get_tok_detail(accessToken, tokId)

        response.enqueue(object : Callback<CommunityTokDetailDto> {
            override fun onResponse(call: Call<CommunityTokDetailDto>, response: Response<CommunityTokDetailDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _tok.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityTokDetailDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    //투표 보내기
    fun post_vote(list: List<Int>, voteId: String) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.post_vote(accessToken, voteId, CommunityVotePost(list))

        response.enqueue(object : Callback<CommunityVoteResult> {
            override fun onResponse(call: Call<CommunityVoteResult>, response: Response<CommunityVoteResult>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _votePeopleEachOption.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityVoteResult>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    //투표 종료하기
    fun end_vote(voteId: String) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.end_vote(accessToken, voteId)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * tok 삭제 API
     * */
    fun delete_tok(tokId : String) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.delete_tok(accessToken, tokId)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**엑세스 토큰 체크*/
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()
        val response = accessTokenRepository.checkAccessToken(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _accessToken.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    _accessToken.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}