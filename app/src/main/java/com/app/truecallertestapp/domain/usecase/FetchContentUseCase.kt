package com.app.truecallertestapp.domain.usecase

import com.app.truecallertestapp.data.common.Resource
import com.app.truecallertestapp.domain.repository.ContentRepository
import javax.inject.Inject

class FetchContentUseCase @Inject constructor(
    private val repository: ContentRepository
) {
    /**
     * Executes the use case.
     * @param url The URL from which to fetch and parse content.
     * @return A Resource wrapper containing the plain text or an error.
     */
    suspend operator fun invoke(url: String): Resource<String> {
        return repository.getPlainTextFromUrl(url)
    }
}