package com.app.truecallertestapp.domain.repository

import com.app.truecallertestapp.data.common.Resource

interface ContentRepository {
    suspend fun getPlainTextFromUrl(url: String): Resource<String>
}