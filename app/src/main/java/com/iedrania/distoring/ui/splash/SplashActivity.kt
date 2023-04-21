package com.iedrania.distoring.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.iedrania.distoring.R
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.ui.login.LoginActivity
import com.iedrania.distoring.ui.main.MainActivity
import com.iedrania.distoring.ui.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val pref = LoginPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[MainViewModel::class.java]

        var intent: Intent? = null
        mainViewModel.getLoginInfo().observe(this) { token ->
            intent = if (!token.isNullOrEmpty()) {
                (Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                (Intent(this@SplashActivity, LoginActivity::class.java))
            }
        }

        val runnable = Runnable {
            startActivity(intent)
            finish()
        }
        findViewById<View>(android.R.id.content).postDelayed(runnable, 1000L)
    }
}