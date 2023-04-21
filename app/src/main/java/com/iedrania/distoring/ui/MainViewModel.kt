package com.iedrania.distoring.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iedrania.distoring.helper.LoginPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: LoginPreferences) : ViewModel() {
    fun getSessionInfo(): LiveData<Boolean> {
        return pref.getLoginSession().asLiveData()
    }

    fun getLoginInfo(): LiveData<String> {
        return pref.getLoginToken().asLiveData()
    }

    fun saveSessionInfo(token: Boolean) {
        viewModelScope.launch {
            pref.saveSessionToken(token)
        }
    }

    fun saveLoginInfo(token: String) {
        viewModelScope.launch {
            pref.saveLoginToken(token)
        }
    }
}