package com.iedrania.distoring.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iedrania.distoring.helper.LoginPreferences

class MainViewModel(private val pref: LoginPreferences) : ViewModel() {
    fun getLoginInfo(): LiveData<String> {
        return pref.getLoginToken().asLiveData()
    }
}