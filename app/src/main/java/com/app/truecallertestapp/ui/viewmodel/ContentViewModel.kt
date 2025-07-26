package com.app.truecallertestapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.truecallertestapp.common.AppConstants
import com.app.truecallertestapp.common.TextAnalysisHelper
import com.app.truecallertestapp.data.common.Resource
import com.app.truecallertestapp.di.IoDispatcher
import com.app.truecallertestapp.domain.usecase.FetchContentUseCase
import com.app.truecallertestapp.ui.events.ContentIntent
import com.app.truecallertestapp.ui.state.ContentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val fetchContentUseCase: FetchContentUseCase,
    private val textHelper: TextAnalysisHelper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

) : ViewModel() {
    private val _contentState = MutableStateFlow(ContentState())
    val contentState: StateFlow<ContentState> = _contentState.asStateFlow()

    fun handleIntent(intent: ContentIntent) {
        when (intent) {
            is ContentIntent.FetchContentButtonClicked -> {
                fetchContent()
            }
        }
    }

    private fun fetchContent() {
        viewModelScope.launch(ioDispatcher) {
            _contentState.update {
                it.copy(isLoading = true, error = null)
            }
            var errorCount = 0

            val char15thDeferred = async {
                when (val res = fetchContentUseCase.invoke(AppConstants.WEBSITE_URL)) {
                    is Resource.Success -> textHelper.get15thCharacter(res.data)
                    is Resource.Error -> {
                        errorCount++
                        null
                    }
                }
            }

            val every15thDeferred = async {
                when (val res = fetchContentUseCase.invoke(AppConstants.WEBSITE_URL)) {
                    is Resource.Success -> textHelper.getEvery15thCharacter(res.data)
                    is Resource.Error -> {
                        errorCount++
                        null
                    }
                }
            }

            val wordFreqDeferred = async {
                when (val res = fetchContentUseCase.invoke(AppConstants.WEBSITE_URL)) {
                    is Resource.Success -> textHelper.getWordFrequencies(res.data)
                    is Resource.Error -> {
                        errorCount++
                        null
                    }
                }
            }

            val char15th = char15thDeferred.await()
            char15th?.let {char ->
                _contentState.update { state -> state.copy(char15th = char) }
            }

            val every15th = every15thDeferred.await()
            every15th?.let {char->
                _contentState.update { state -> state.copy(every15th = char)}
            }

            val wordFreq = wordFreqDeferred.await()
            wordFreq?.let {words->
                _contentState.update { state -> state.copy(wordCounts = words)}
            }
            if (errorCount == 3) {
                _contentState.update {
                    it.copy(error = "Failed to fetch content from all sources", isLoading = false)
                }
            } else {
                _contentState.update { it.copy(isLoading = false) }
            }

        }
    }

}