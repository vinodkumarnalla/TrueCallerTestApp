package com.app.truecallertestapp.ui.events


sealed class ContentIntent {
    object FetchContentButtonClicked : ContentIntent()

}