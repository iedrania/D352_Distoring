package com.iedrania.distoring.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.iedrania.distoring.ui.components.EmailEditText
import com.iedrania.distoring.ui.components.PasswordEditText
import com.iedrania.distoring.R
import com.iedrania.distoring.ui.components.SubmitButton

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
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnable()
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