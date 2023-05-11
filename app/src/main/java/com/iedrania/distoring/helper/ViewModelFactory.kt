package com.iedrania.distoring.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iedrania.distoring.di.Injection
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.StoryViewModel

class ViewModelFactory(
    private val pref: LoginPreferences?, private val token: String?, private val context: Context?
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MainViewModel(pref!!) as T
        } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return StoryViewModel(
                Injection.provideRepository(
                    context!!, token ?: ""
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
