package com.example.hackernews.app

import android.app.Application

interface InspectorInitializer {
    fun initialize(application: Application)
}