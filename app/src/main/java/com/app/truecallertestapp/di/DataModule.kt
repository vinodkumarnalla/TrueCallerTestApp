package com.app.truecallertestapp.di

import com.app.truecallertestapp.data.datasource.RemoteDataSource
import com.app.truecallertestapp.data.datasource.RemoteDataSourceImpl
import com.app.truecallertestapp.data.parser.HtmlParser
import com.app.truecallertestapp.data.parser.JsoupHtmlParser
import com.app.truecallertestapp.data.repository.ContentRepositoryImpl
import com.app.truecallertestapp.domain.repository.ContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindContentRepository(
        htmlRepositoryImpl: ContentRepositoryImpl
    ): ContentRepository

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        htmlRemoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource
}



@Module
@InstallIn(SingletonComponent::class)
abstract class HtmlParserModule {

    @Binds
    @Singleton
    abstract fun bindHtmlParser(
        jsoupHtmlParser: JsoupHtmlParser
    ): HtmlParser
}
