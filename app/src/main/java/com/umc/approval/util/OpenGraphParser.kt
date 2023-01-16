package com.umc.approval.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object OpenGraphParser {
    suspend fun parse(url: String): Map<String, String> = withContext(Dispatchers.IO) {
        var connection: HttpsURLConnection? = null

        try {
            if(!url.contains("http")){
                connection = URL(StringBuilder("https://").append(url).toString()).openConnection() as HttpsURLConnection
            }else connection = URL(url).openConnection() as HttpsURLConnection

            connection.connect()

            val result = connection.inputStream?.run {
                val reader = BufferedReader(InputStreamReader(this))
                val buffer = StringBuffer()

                var line: String? = ""
                while (reader.readLine().also { line = it } != null) {
                    buffer.append(line)
                }
                buffer.toString()
            }

            parseOgTag(result ?: "")
        } catch (ex: Exception) {
            Log.d("openGraphParser", ex.toString())
            return@withContext mapOf<String, String>()
        } finally {
            connection?.disconnect()
        }
    }

    private fun parseOgTag(html: String): Map<String, String> {
        val ogTags = mutableMapOf<String, String>()
        Log.d("로그",html);
        Regex("<meta property[^>]([^<]*)>").findAll(html).forEach {
            val metaProperty = it.groupValues.getOrNull(1) ?: ""

            Regex("\"og:(.*)\" content=\"(.*)\"").find(metaProperty)?.let {
                val ogType = it.groupValues.getOrNull(1)
                val content = it.groupValues.getOrNull(2)

                if (ogType != null && content != null) {
                    ogTags[ogType] = content
                }
            }
        }

        return ogTags
    }
    fun concat(s1: String, s2: String): String {
        return StringBuilder(s1).append(s2).toString()
    }
}