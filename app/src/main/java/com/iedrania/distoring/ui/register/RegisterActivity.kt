package com.iedrania.distoring.ui.register

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
import com.iedrania.distoring.databinding.ActivityRegisterBinding
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.main.MainActivity

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
                finish()
            }
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.isError.observe(this) { showError(it) }
        mainViewModel.isFail.observe(this) { showFailure(it) }

        setRegisterButtonEnable()

        binding.edRegisterName.doOnTextChanged { _, _, _, _ ->
            setRegisterButtonEnable()
        }
        binding.edRegisterEmail.doOnTextChanged { _, _, _, _ ->
            setRegisterButtonEnable()
        }
        binding.edRegisterPassword.doOnTextChanged { _, _, _, _ ->
            setRegisterButtonEnable()
        }
        binding.btnRegisterSubmit.setOnClickListener {
            mainViewModel.postRegister(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        binding.btnRegisterLogin.setOnClickListener { finish() }
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
                this@RegisterActivity, getString(R.string.register_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showFailure(isFail: Boolean) {
        if (isFail) {
            Toast.makeText(
                this@RegisterActivity, getString(R.string.retrofit_fail), Toast.LENGTH_SHORT
            ).show()
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
}