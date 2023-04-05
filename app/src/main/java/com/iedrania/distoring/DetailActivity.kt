package com.iedrania.distoring

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iedrania.distoring.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, Story::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            val text = "${story.name} ${story.photoURL} ${story.description} ${story.createdAt}"
            binding.tvObject.text = text
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}