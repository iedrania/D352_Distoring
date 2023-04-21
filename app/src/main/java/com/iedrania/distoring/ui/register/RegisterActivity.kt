package com.iedrania.distoring.ui.register

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
import com.iedrania.distoring.data.model.LoginResponse
import com.iedrania.distoring.data.model.RegisterResponse
import com.iedrania.distoring.data.retrofit.ApiConfig
import com.iedrania.distoring.databinding.ActivityRegisterBinding
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.login.LoginActivity
import com.iedrania.distoring.ui.main.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = LoginPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[MainViewModel::class.java]
        mainViewModel.getSessionInfo().observe(this) { isLogin ->
            if (isLogin) {
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        setRegisterButtonEnable()

        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.btnRegisterSubmit.setOnClickListener {
            postRegister(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.btnRegisterLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postRegister(name: String, email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService("").postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    postLogin(email, password)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Log.e(TAG, "onFailure: $errorMessage")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
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
                    mainViewModel.saveSessionInfo(true)
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

    private fun setRegisterButtonEnable() {
        val nameResult = binding.edRegisterName.text
        val emailResult = binding.edRegisterEmail.text
        val passwordResult = binding.edRegisterPassword.text
        binding.btnRegisterSubmit.isEnabled = nameResult != null && nameResult.toString()
            .isNotBlank() && emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString()
            .isNotBlank() && passwordResult.toString().length >= 8
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}