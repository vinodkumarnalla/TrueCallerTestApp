package com.app.truecallertestapp.common

import javax.inject.Inject

class TextAnalysisHelper @Inject constructor() {

    fun get15thCharacter(text: String): Char? =
        text.getOrNull(14)

    fun getEvery15thCharacter(text: String): List<Char> =
        text.withIndex()
            .filter { (index, _) -> ((index + 1) % 15 == 0 && !text[index].isWhitespace())}
            .map { it.value }

    fun getWordFrequencies(text: String): Map<String, Int> =
        text
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
            .map { it.lowercase() }
            .groupingBy { it }
            .eachCount()
}
