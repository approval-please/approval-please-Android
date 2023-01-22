package com.umc.approval.util

import android.util.Log
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**Url Crawling*/
object CrawlingTask {

    fun getElements(url: String) : Elements? {
        var document: Document? = null
        var elements: Elements? = null
        try {
            document = Jsoup.connect(url).get()
            document?.let {
                elements = document.select("meta[property^=og:]")
            } ?: run {
                return null
            }
        } catch (httpException: HttpStatusException) {
            Log.e("http exception", "${httpException.message}")
        } catch(e: Exception) { e.printStackTrace() }
        return elements
    }
}