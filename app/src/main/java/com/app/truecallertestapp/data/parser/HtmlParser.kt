package com.app.truecallertestapp.data.parser

import org.jsoup.Jsoup
import javax.inject.Inject

interface HtmlParser {
    fun getPlainText(html: String): String
}


class JsoupHtmlParser @Inject constructor() : HtmlParser {
    override fun getPlainText(html: String): String {
        return Jsoup.parse(html).body().text()
    }
}