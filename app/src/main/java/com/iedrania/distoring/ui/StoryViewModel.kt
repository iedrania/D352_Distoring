package com.iedrania.distoring.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iedrania.distoring.data.StoryRepository
import com.iedrania.distoring.data.model.Story
import kotlinx.coroutines.flow.Flow

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: Flow<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)

}
