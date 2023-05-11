package com.iedrania.distoring.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iedrania.distoring.data.StoryRepository
import com.iedrania.distoring.data.model.*
import com.iedrania.distoring.di.Injection
import kotlinx.coroutines.flow.Flow

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: Flow<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)

}
