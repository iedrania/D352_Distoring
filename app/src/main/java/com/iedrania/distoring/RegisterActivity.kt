package com.iedrania.distoring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerButton: SubmitButton
    private lateinit var nameEditText: NameEditText
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.register_button)
        nameEditText = findViewById(R.id.ed_register_name)
        emailEditText = findViewById(R.id.ed_register_email)
        passwordEditText = findViewById(R.id.ed_register_password)

        setRegisterButtonEnable()

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailResult = emailEditText.text
                val passwordResult = passwordEditText.text
                if (emailResult != null && passwordResult != null && emailResult.toString()
                        .isNotBlank() && passwordResult.toString().isNotBlank()
                ) {
                    setRegisterButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val nameResult = nameEditText.text
                val passwordResult = passwordEditText.text
                if (nameResult != null && passwordResult != null && nameResult.toString()
                        .isNotBlank() && passwordResult.toString().isNotBlank()
                ) {
                    setRegisterButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val nameResult = nameEditText.text
                val emailResult = emailEditText.text
                if (nameResult != null && emailResult != null && nameResult.toString()
                        .isNotBlank() && emailResult.toString().isNotBlank()
                ) {
                    setRegisterButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        registerButton.setOnClickListener {
            Toast.makeText(
                this@RegisterActivity,
                "${nameEditText.text} ${emailEditText.text} ${passwordEditText.text}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setRegisterButtonEnable() {
        val nameResult = nameEditText.text
        val emailResult = emailEditText.text
        val passwordResult = passwordEditText.text
        registerButton.isEnabled = nameResult != null && nameResult.toString()
            .isNotBlank() && emailResult != null && emailResult.toString()
            .isNotBlank() && passwordResult != null && passwordResult.toString().isNotBlank()
    }
}