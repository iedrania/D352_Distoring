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
import java.text.SimpleDateFormat
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val pref = LoginPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
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
            intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.getDefault())
            val date = inputFormat.parse(story.createdAt)
            binding.tvDetailDate.text = date?.let { outputFormat.format(it) }

            binding.tvDetailName.text = story.name
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .transform(RoundedCorners(64))
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