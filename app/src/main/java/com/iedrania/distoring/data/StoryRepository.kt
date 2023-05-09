package com.iedrania.distoring.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.iedrania.distoring.database.StoryDatabase
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.data.retrofit.ApiService

class StoryRepository(
    private val token: String,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(config = PagingConfig(
            pageSize = 10
        ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }).liveData
    }
}