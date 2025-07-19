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

            when (val response = fetchContentUseCase.invoke(AppConstants.WEBSITE_URL)) {
                is Resource.Success -> {
                    _contentState.update { it.copy(isLoading = false) }

                    val text = response.data

                    val char15thDeferred = async { textHelper.get15thCharacter(text) }
                    val every15thDeferred = async { textHelper.getEvery15thCharacter(text) }
                    val wordFreqDeferred = async { textHelper.getWordFrequencies(text) }

                    // Await and update state as each completes
                    val char15th = char15thDeferred.await()
                    _contentState.update { it.copy(char15th = char15th) }

                    val every15th = every15thDeferred.await()
                    _contentState.update { it.copy(every15th = every15th) }

                    val wordFreq = wordFreqDeferred.await()
                    _contentState.update {
                        it.copy(
                            wordCounts = wordFreq,
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _contentState.update {
                        it.copy(
                            error = response.message ,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

}