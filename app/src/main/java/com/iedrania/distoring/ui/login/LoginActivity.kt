package com.iedrania.distoring.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.data.model.LoginResponse
import com.iedrania.distoring.data.retrofit.ApiConfig
import com.iedrania.distoring.databinding.ActivityLoginBinding
import com.iedrania.distoring.ui.main.MainActivity
import com.iedrania.distoring.ui.main.MainViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setLoginButtonEnable()

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.loginButton.setOnClickListener {
            postLogin(binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val pref = LoginPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[MainViewModel::class.java]
        mainViewModel.getLoginInfo().observe(this) { token ->
            if (token != "") {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun postLogin(email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService("").postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    mainViewModel.saveLoginInfo(responseBody.loginResult.token)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "onFailure: $errorMessage")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setLoginButtonEnable() {
        val emailResult = binding.edLoginEmail.text
        val passwordResult = binding.edLoginPassword.text
        binding.loginButton.isEnabled = emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString().isNotBlank()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}