package com.umc.approval.data.dto.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyword_table")
data class KeywordDto (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "keyword")
        var keyword: String,
        @ColumnInfo(name = "writer")
        var writer: String,
)