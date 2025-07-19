package com.app.truecallertestapp.data.datasource

import com.app.truecallertestapp.data.common.HttpClientHelper
import com.app.truecallertestapp.data.common.Resource
import com.app.truecallertestapp.data.parser.HtmlParser
import javax.inject.Inject
import javax.inject.Singleton


interface RemoteDataSource {
    /**
     * Fetches HTML content from the given URL.
     * @param url The URL to fetch HTML from.
     * @return The HTML content as a String
     */
    suspend fun fetchHtml(url: String):  Resource<String>
}

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val httpClientHelper: HttpClientHelper,
    private val htmlParser: HtmlParser
) : RemoteDataSource {

    override suspend fun fetchHtml(url: String): Resource<String> {
        return when (val result = httpClientHelper.get(url)) {
            is Resource.Success -> {
                val plainText = htmlParser.getPlainText(result.data)
                Resource.Success(plainText)
            }
            is Resource.Error -> result
        }
    }
}