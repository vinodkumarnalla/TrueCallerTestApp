package com.app.truecallertestapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.app.truecallertestapp.ui.screens.HomeScreenContent
import com.app.truecallertestapp.ui.state.ContentState
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsLoadingIndicator_whenLoading() {
        val fakeState = ContentState(isLoading = true)

        composeTestRule.setContent {
            HomeScreenContent(state = fakeState, onFetchClicked = {})
        }

        composeTestRule
            .onNodeWithTag("LoadingIndicator")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsErrorMessage_whenErrorExists() {
        val errorMessage = "Something went wrong"
        val fakeState = ContentState(error = errorMessage)

        composeTestRule.setContent {
            HomeScreenContent(state = fakeState, onFetchClicked = {})
        }

        composeTestRule
            .onNodeWithTag("errorTest")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsChar15thSection() {
        val fakeChar15th = 'X'
        val fakeState = ContentState(char15th = fakeChar15th)

        composeTestRule.setContent {
            HomeScreenContent(state = fakeState, onFetchClicked = {})
        }

        composeTestRule
            .onNodeWithText("15th Character: $fakeChar15th")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_disablesFetchButton_whenLoading() {
        val fakeState = ContentState(isLoading = true)

        composeTestRule.setContent {
            HomeScreenContent(state = fakeState, onFetchClicked = {})
        }

        composeTestRule
            .onNodeWithTag("fetchButton")
            .assertIsNotEnabled()
    }

    @Test
    fun homeScreen_callsViewModel_onFetchButtonClick() {
        var wasClicked = false
        val fakeState = ContentState()

        composeTestRule.setContent {
            HomeScreenContent(state = fakeState, onFetchClicked = {
                wasClicked = true
            })
        }

        composeTestRule
            .onNodeWithTag("fetchButton")
            .performClick()

        assertTrue(wasClicked)
    }
}