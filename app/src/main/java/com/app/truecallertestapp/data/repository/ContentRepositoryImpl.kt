package com.app.truecallertestapp.data.repository

import com.app.truecallertestapp.data.common.Resource
import com.app.truecallertestapp.data.datasource.RemoteDataSource
import com.app.truecallertestapp.domain.repository.ContentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ContentRepository {

    override suspend fun getPlainTextFromUrl(url: String): Resource<String> {
       return remoteDataSource.fetchHtml(url)
    }
}