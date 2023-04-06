package com.iedrania.distoring.ui.register

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.iedrania.distoring.ui.components.EmailEditText
import com.iedrania.distoring.ui.components.NameEditText
import com.iedrania.distoring.ui.components.PasswordEditText
import com.iedrania.distoring.ui.components.SubmitButton
import com.iedrania.distoring.data.model.RegisterResponse
import com.iedrania.distoring.data.retrofit.ApiConfig
import com.iedrania.distoring.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var registerButton: SubmitButton
    private lateinit var nameEditText: NameEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton = binding.registerButton
        nameEditText = binding.edRegisterName
        emailEditText = binding.edRegisterEmail
        passwordEditText = binding.edRegisterPassword

        setRegisterButtonEnable()

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        registerButton.setOnClickListener {
            postRegister(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun postRegister(name: String, email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    // TODO register berhasil
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
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
        val nameResult = nameEditText.text
        val emailResult = emailEditText.text
        val passwordResult = passwordEditText.text
        registerButton.isEnabled = nameResult != null && nameResult.toString()
            .isNotBlank() && emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString()
            .isNotBlank() && passwordResult.toString().length >= 8
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}