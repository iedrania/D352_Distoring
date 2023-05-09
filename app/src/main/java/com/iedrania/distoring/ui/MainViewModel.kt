package com.iedrania.distoring.ui

import android.util.Log
import androidx.lifecycle.*
import com.iedrania.distoring.data.model.*
import com.iedrania.distoring.data.retrofit.ApiConfig
import com.iedrania.distoring.helper.LoginPreferences
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel(private val pref: LoginPreferences) : ViewModel() {
    private val _listStory = MutableLiveData<List<Story>>()
    val listStory: LiveData<List<Story>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun postRegister(name: String, email: String, password: String) {
        _isError.value = false
        _isFail.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService("").postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    postLogin(email, password)
                } else {
                    _isError.value = true
                    Log.e(TAG, "postRegister ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postRegister onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _isFail.value = true
                Log.e(TAG, "postRegister onFailure: ${t.message}")
            }
        })
    }

    fun postLogin(email: String, password: String) {
        _isError.value = false
        _isFail.value = false
        _isLoading.value = true
        val client = ApiConfig.getApiService("").postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    saveSessionInfo(true)
                    saveLoginInfo(responseBody.loginResult.token)
                } else {
                    _isError.value = true
                    Log.e(TAG, "postLogin ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postLogin onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isFail.value = true
                Log.e(TAG, "postLogin onFailure: ${t.message}")
            }
        })
    }

//    fun findStories(token: String) {
//        _isFail.value = false
//        _isLoading.value = true
//        val client = ApiConfig.getApiService(token).getStory()
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//                call: Call<StoryResponse>, response: Response<StoryResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (response.isSuccessful && responseBody != null) {
//                        _listStory.value = responseBody.listStory
//                    }
//                } else {
//                    Log.e(TAG, "findStories ERROR: ${response.message()}")
//                    val errorBody = response.errorBody()?.string()
//                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
//                    Log.e(TAG, "findStories onError: $errorMessage")
//                }
//            }
//
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                _isLoading.value = false
//                _isFail.value = true
//                Log.e(TAG, "findStories onFailure: ${t.message}")
//            }
//        })
//    }

    fun postStory(token: String, file: File, desc: String) {
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", file.name, requestImageFile
        )

        _isSuccess.value = false
        _isError.value = false
        _isFail.value = false
        _isLoading.value = true
        val service = ApiConfig.getApiService(token).uploadStory(imageMultipart, description)
        service.enqueue(object : Callback<PostStoryResponse> {
            override fun onResponse(
                call: Call<PostStoryResponse>, response: Response<PostStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isSuccess.value = true
                } else {
                    _isError.value = true
                    Log.e(TAG, "postStory ERROR: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "postStory onError: $errorMessage")
                }
            }

            override fun onFailure(call: Call<PostStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isFail.value = true
                Log.e(TAG, "postStory onFailure: ${t.message}")
            }
        })
    }

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

    companion object {
        private const val TAG = "MainViewModel"
    }
}