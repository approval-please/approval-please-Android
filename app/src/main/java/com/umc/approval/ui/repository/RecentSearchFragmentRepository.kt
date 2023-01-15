package com.umc.approval.ui.repository

import com.umc.approval.db.RecentSearchDatabase
import com.umc.approval.ui.dto.KeywordDto

/**
 * RecentSearch Fragment Repository
 * */
class RecentSearchFragmentRepository(private val db: RecentSearchDatabase) {

    //최근 8개의 검색어를 가지고 오는 메서드
    suspend fun getAllKeywords() = db.recentSearchDao().getKeywords()

    //최근 검색어중 선택한 하나를 삭제하는 메서드
    suspend fun deleteOne(keyword: KeywordDto) = db.recentSearchDao().deleteKeyword(keyword)

    //최근 검색어 DB에 추가하는 메서드
    suspend fun insertData(keyword: KeywordDto) =
        db.recentSearchDao().insertKeyword(keyword)

    //모든 최근 검색어 삭제하는 메서드
    suspend fun deleteAll() =
        db.recentSearchDao().deleteAllKeywords()

    //연관 검색어 8개 가지고 오기
    suspend fun getAllRelatedKeyword(query: String) = db.recentSearchDao().getRelatedKeywords(query)
}