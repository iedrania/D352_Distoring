package com.iedrania.distoring.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.iedrania.distoring.R
import com.iedrania.distoring.databinding.ActivityLoginBinding
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.main.MainActivity
import com.iedrania.distoring.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = LoginPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[MainViewModel::class.java]
        mainViewModel.getSessionInfo().observe(this) { isLogin ->
            if (isLogin) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.isError.observe(this) { showError(it) }
        mainViewModel.isFail.observe(this) { showFailure(it) }

        setLoginButtonEnable()

        binding.edLoginEmail.doOnTextChanged { _, _, _, _ ->
            setLoginButtonEnable()
        }
        binding.edLoginPassword.doOnTextChanged { _, _, _, _ ->
            setLoginButtonEnable()
        }
        binding.btnLoginSubmit.setOnClickListener {
            mainViewModel.postLogin(
                binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString()
            )
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        binding.btnLoginRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(
                this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showFailure(isFail: Boolean) {
        if (isFail) {
            Toast.makeText(
                this@LoginActivity, getString(R.string.retrofit_fail), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setLoginButtonEnable() {
        val emailResult = binding.edLoginEmail.text
        val passwordResult = binding.edLoginPassword.text
        binding.btnLoginSubmit.isEnabled = emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString().isNotBlank()
    }
}