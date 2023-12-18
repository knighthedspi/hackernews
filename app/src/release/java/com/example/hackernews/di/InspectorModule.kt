package com.example.hackernews.di

import android.app.Application
import com.example.hackernews.app.InspectorInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object InspectorModule {
    @Provides
    fun inspectorInitializer(): InspectorInitializer {
        return object : InspectorInitializer {
            override fun initialize(application: Application) {}
        }
    }

}