package com.umc.approval.data.db.dao

import androidx.room.*
import com.umc.approval.data.dto.search.post.KeywordDto

/**
 * 최근 검색어 DAO
 * 데이터 접근 객체, Controller라고 생각하면 됨
 * */
@Dao
interface RecentSearchDao {

    //Keyword Insert 쿼리, 동일한 PK가 있는 경우 덮어씌우기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: KeywordDto)

    //Keyword Delete 쿼리
    @Delete
    suspend fun deleteKeyword(keyword: KeywordDto)

    //모든 최근 검색 Keyword 삭제 쿼리
    @Query("DELETE FROM keyword_table")
    suspend fun deleteAllKeywords()

    //최근 검색 Keyword 8개 가지고 오는 쿼리
    @Query("SELECT * FROM keyword_table ORDER BY id DESC LIMIT 8")
    suspend fun getKeywords(): List<KeywordDto>

    @Query("SELECT * FROM keyword_table WHERE keyword LIKE :query ORDER BY id DESC LIMIT 8")
    suspend fun getRelatedKeywords(query: String): List<KeywordDto>
}