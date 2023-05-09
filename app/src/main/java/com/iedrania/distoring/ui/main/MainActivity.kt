package com.iedrania.distoring.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iedrania.distoring.R
import com.iedrania.distoring.adapter.LoadingStateAdapter
import com.iedrania.distoring.databinding.ActivityMainBinding
import com.iedrania.distoring.helper.LoginPreferences
import com.iedrania.distoring.helper.ViewModelFactory
import com.iedrania.distoring.ui.MainViewModel
import com.iedrania.distoring.ui.StoryViewModel
import com.iedrania.distoring.ui.ViewModelFactory2
import com.iedrania.distoring.ui.add.AddActivity
import com.iedrania.distoring.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""

        val pref = LoginPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref)
        )[MainViewModel::class.java]
        mainViewModel.getSessionInfo().observe(this) { isLogin ->
            if (isLogin) {
                mainViewModel.getLoginInfo().observe(this) {
//                    mainViewModel.findStories(it)
                    storyViewModel = ViewModelProvider(
                        this, ViewModelFactory2(it, this)
                    )[StoryViewModel::class.java]
                    getData()
                }
            } else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
//        mainViewModel.listStory.observe(this) { stories ->
//            setStoryData(stories)
//        }
        mainViewModel.isLoading.observe(this) { showLoading(it) }
        mainViewModel.isFail.observe(this) { showFailure(it) }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun setStoryData(listStory: List<Story>) {
//        if (listStory.isEmpty()) {
//            binding.tvMainEmpty.visibility = View.VISIBLE
//        } else {
//            binding.tvMainEmpty.visibility = View.GONE
//            val adapter = StoryAdapter(listStory)
//            binding.rvStories.adapter = adapter
//        }
//    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
            Log.d("MAIN", "here")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showFailure(isFail: Boolean) {
        if (isFail) {
            Toast.makeText(
                this@MainActivity, getString(R.string.retrofit_fail), Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.saveLoginInfo("")
                mainViewModel.saveSessionInfo(false)
                finish()
            }
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}