package com.app.truecallertestapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.truecallertestapp.R
import com.app.truecallertestapp.ui.events.ContentIntent
import com.app.truecallertestapp.ui.state.ContentState
import com.app.truecallertestapp.ui.viewmodel.ContentViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ContentViewModel = hiltViewModel(),
) {
    val state by viewModel.contentState.collectAsState()

    HomeScreenContent(
        state = state,
        onFetchClicked = {
            viewModel.handleIntent(ContentIntent.FetchContentButtonClicked)
        },
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    state: ContentState,
    onFetchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ErrorMessage(state.error)
            Char15thSection(state.char15th)
            Every15thSection(state.every15th)
            WordFrequenciesSection(state.wordCounts)
        }

        if (state.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("LoadingIndicator")
            )
        }

        FetchButton(
            isEnabled = !state.isLoading,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp).testTag("fetchButton"),
            onClick = onFetchClicked
        )
    }
}


@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
fun FetchButton(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.fetch_content))
    }
}

@Composable
fun ErrorMessage(error: String?) {
    error?.let {
        Text(
            text = "Error: $it",
            color = Color.Red,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("errorTest")
        )
    }
}

@Composable
fun Char15thSection(char: Char?) {
    char?.let {
        Text(
            text = "15th Character: $it",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun Every15thSection(chars: List<Char>) {
    if (chars.isNotEmpty()) {
        Text("Every 15th Character:", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = chars.joinToString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun WordFrequenciesSection(wordCounts: Map<String, Int>) {
    if (wordCounts.isNotEmpty()) {
        Text("Word Frequencies:", style = MaterialTheme.typography.headlineMedium)
        wordCounts.entries
            .sortedByDescending { it.value }
            .forEach {
                Text("${it.key}: ${it.value}", style = MaterialTheme.typography.bodyMedium)
            }
    }
}

