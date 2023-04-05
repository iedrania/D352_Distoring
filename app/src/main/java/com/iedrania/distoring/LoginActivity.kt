package com.iedrania.distoring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: SubmitButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        emailEditText = findViewById(R.id.ed_login_email)
        passwordEditText = findViewById(R.id.ed_login_password)

        setLoginButtonEnable()

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val passwordResult = passwordEditText.text
                if (passwordResult != null && passwordResult.toString().isNotBlank()) {
                    setLoginButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailResult = emailEditText.text
                if (emailResult != null && emailResult.toString().isNotBlank()) {
                    setLoginButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        loginButton.setOnClickListener {
            Toast.makeText(
                this@LoginActivity,
                "${emailEditText.text} ${passwordEditText.text}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setLoginButtonEnable() {
        val emailResult = emailEditText.text
        val passwordResult = passwordEditText.text
        loginButton.isEnabled = emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString().isNotBlank()
    }
}