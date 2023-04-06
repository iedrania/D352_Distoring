package com.iedrania.distoring.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, Story::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            binding.tvDetailDate.text = story.createdAt
            binding.tvDetailName.text = story.name
            Glide.with(this@DetailActivity)
                .load(story.photoURL)
                .transform(RoundedCorners(32))
                .into(binding.ivDetailPhoto)
            binding.tvDetailDescription.text = story.description
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}