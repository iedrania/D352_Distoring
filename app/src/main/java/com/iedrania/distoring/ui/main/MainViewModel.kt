package com.iedrania.distoring.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iedrania.distoring.helper.LoginPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: LoginPreferences) : ViewModel() {
    fun getLoginInfo(): LiveData<String> {
        return pref.getLoginToken().asLiveData()
    }

    fun saveLoginInfo(token: String) {
        viewModelScope.launch {
            pref.saveLoginToken(token)
        }
    }
}