package com.iedrania.distoring.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iedrania.distoring.data.StoryRepository
import com.iedrania.distoring.data.model.*
import com.iedrania.distoring.di.Injection

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)

}

class ViewModelFactory2(private val token: String?, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return StoryViewModel(
                Injection.provideRepository(
                    context, token ?: ""
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
