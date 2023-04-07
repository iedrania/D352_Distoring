package com.iedrania.distoring.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.iedrania.distoring.helper.LoginPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: LoginPreferences) : ViewModel() {
    fun getLoginInfo(): LiveData<String> {
        return pref.getLoginToken().asLiveData()
    }

    fun saveLoginInfo(token: String) {
        viewModelScope.launch {
            pref.saveLoginToken(token)
        }
    }
}