package com.app.truecallertestapp

import com.app.truecallertestapp.common.AppConstants
import com.app.truecallertestapp.common.TextAnalysisHelper
import com.app.truecallertestapp.data.common.Resource
import com.app.truecallertestapp.domain.usecase.FetchContentUseCase
import com.app.truecallertestapp.ui.events.ContentIntent
import com.app.truecallertestapp.ui.viewmodel.ContentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ContentViewModelTest {


    @Mock
    private lateinit var fetchContentUseCase: FetchContentUseCase

    private lateinit var textHelper: TextAnalysisHelper

    private lateinit var testScheduler: TestCoroutineScheduler
    private lateinit var testIoDispatcher: TestDispatcher
    private lateinit var testMainDispatcher: TestDispatcher

    private lateinit var viewModel: ContentViewModel

    @Before
    fun setUp() {
        testScheduler = TestCoroutineScheduler()
        testIoDispatcher = StandardTestDispatcher(testScheduler)
        testMainDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testMainDispatcher)
        textHelper = TextAnalysisHelper()
        viewModel = ContentViewModel(fetchContentUseCase, textHelper, testIoDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchContentSuccessTestCase() = runTest(testScheduler) {
        val mockText = "This is some sample fetched text data."
        val mock15thChar = 'a'
        val mockEvery15th = listOf('a', 'e')
        val mockWordFreq = mapOf("this" to 1, "is" to 1,"some" to 1,"sample" to 1,"fetched" to 1,"text" to 1, "data" to 1)

        `when`(fetchContentUseCase.invoke(AppConstants.WEBSITE_URL)).thenReturn(Resource.Success(mockText))
        viewModel.handleIntent(ContentIntent.FetchContentButtonClicked)

        var currentState = viewModel.contentState.value
        assertNull("Error should be null initially", currentState.error)

        testScheduler.advanceUntilIdle()

        currentState = viewModel.contentState.value
        assertFalse("isLoading should be false in final state", currentState.isLoading)
        assertNull("error should be null in final state", currentState.error)
        assertEquals("char15th should be correct", mock15thChar, currentState.char15th)
        assertEquals("every15th should be correct", mockEvery15th, currentState.every15th)
        assertEquals("wordCounts should be correct", mockWordFreq, currentState.wordCounts)
    }

    @Test
    fun fetchContentErrorTestCase() = runTest(testScheduler) {

        val errorMsg = "Network Error"
        `when`(fetchContentUseCase.invoke(AppConstants.WEBSITE_URL)).thenReturn(Resource.Error(errorMsg))
        viewModel.handleIntent(ContentIntent.FetchContentButtonClicked)

        var currentState = viewModel.contentState.value
        assertNull("Error should be null initially", currentState.error)

        testScheduler.advanceUntilIdle()

        currentState = viewModel.contentState.value
        assertFalse("isLoading should be false in final state", currentState.isLoading)
        assertEquals("error should be there in final state", currentState.error ,errorMsg)
    }
}

