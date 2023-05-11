package com.iedrania.distoring.data

import androidx.paging.*
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.data.retrofit.ApiService
import com.iedrania.distoring.database.StoryDatabase
import kotlinx.coroutines.flow.Flow

class StoryRepository(
    private val token: String,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(): Flow<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class) return Pager(config = PagingConfig(
            pageSize = 10
        ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }).flow
    }
}