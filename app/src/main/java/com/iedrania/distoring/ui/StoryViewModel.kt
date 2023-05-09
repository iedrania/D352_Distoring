package com.iedrania.distoring.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iedrania.distoring.data.StoryRepository
import com.iedrania.distoring.data.model.*
import com.iedrania.distoring.di.Injection

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> =
        storyRepository.getStories().cachedIn(viewModelScope)

}

class ViewModelFactory2(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLS1Cc1EtdzdGSHlDR2ZXclkiLCJpYXQiOjE2ODM2MjkzODl9.iYuzV8qG1TdKzBK_E28urib_ne8qaw8nrB1IrRGgkw8")) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
