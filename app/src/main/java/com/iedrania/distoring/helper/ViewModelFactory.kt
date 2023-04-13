package com.iedrania.distoring.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iedrania.distoring.ui.main.MainViewModel

class ViewModelFactory(private val pref: LoginPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}