package com.umc.approval.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umc.approval.data.db.dao.RecentSearchDao
import com.umc.approval.data.dto.KeywordDto

/**
 * 검색 Keyword를 저장할 Database
 * */
@Database(
    entities = [KeywordDto::class],
    version = 2,
    exportSchema = false
)
abstract class RecentSearchDatabase : RoomDatabase() {

    //Room에서 사용할 dao 지정
    abstract fun recentSearchDao(): RecentSearchDao

    //DB 객체도 생성하는데 비용이 많이 들기 때문에 중복으로 생성하지 않도록 싱글톤 설정
    companion object {

        //Multi-Thread 환경을 대비하기 위해 사용
        @Volatile
        private var INSTANCE: RecentSearchDatabase? = null

        private fun buildDatabase(context: Context): RecentSearchDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                RecentSearchDatabase::class.java,
                "recent_keywords_db"
            ).build()

        fun getInstance(context: Context): RecentSearchDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}