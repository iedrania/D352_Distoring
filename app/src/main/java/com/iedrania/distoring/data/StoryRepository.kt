package com.iedrania.distoring.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.iedrania.distoring.database.StoryDatabase
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.data.retrofit.ApiService

class StoryRepository(
    private val storyDatabase: StoryDatabase, private val apiService: ApiService
) {
    fun getStories(): LiveData<PagingData<Story>> {
        Log.d("REPOSITORY", "here")
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}