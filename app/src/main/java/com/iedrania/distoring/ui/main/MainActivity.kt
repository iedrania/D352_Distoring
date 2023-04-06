package com.iedrania.distoring.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iedrania.distoring.R
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.data.model.StoryResponse
import com.iedrania.distoring.data.retrofit.ApiConfig
import com.iedrania.distoring.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        findStories()
    }

    private fun findStories() {
        showLoading(true)
        val client = ApiConfig.getApiService().getStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>, response: Response<StoryResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setStoryData(responseBody.listStory)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setStoryData(listStory: List<Story>) {
        val adapter = StoryAdapter(listStory)
        binding.rvStories.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}