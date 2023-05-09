package com.iedrania.distoring.di

import android.content.Context
import com.iedrania.distoring.data.StoryRepository
import com.iedrania.distoring.data.retrofit.ApiConfig
import com.iedrania.distoring.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context, token: String): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(token, database, apiService)
    }
}