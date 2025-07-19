package com.app.truecallertestapp.ui.state

data class ContentState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val char15th: Char? = null,
    val every15th: List<Char> = emptyList(),
    val wordCounts: Map<String, Int> = emptyMap(),
)