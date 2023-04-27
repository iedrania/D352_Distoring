package com.iedrania.distoring.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.databinding.ActivityDetailBinding
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val pref = LoginPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[MainViewModel::class.java]
        mainViewModel.getSessionInfo().observe(this) {
            if (!it) {
                val intent = Intent(this@DetailActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, Story::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            binding.tvDetailDate.text = story.createdAt
            binding.tvDetailName.text = story.name
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .transform(RoundedCorners(32))
                .into(binding.ivDetailPhoto)
            binding.tvDetailDescription.text = story.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}