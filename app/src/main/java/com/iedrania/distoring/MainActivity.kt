package com.iedrania.distoring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.iedrania.distoring.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Story>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.setHasFixedSize(true)

        list.addAll(getListStories())
        showRecyclerList()
    }

    private fun getListStories(): ArrayList<Story> {
        val listStory = ArrayList<Story>()
        val story = Story(
            "", "", "", ""
        )
        listStory.add(story)
        return listStory
    }

    private fun showRecyclerList() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        val storyAdapter = StoryAdapter(list)
        binding.rvStories.adapter = storyAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}