package com.dickyarya.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dickyarya.storyapp.R
import com.dickyarya.storyapp.databinding.ActivityStoryBinding
import com.dickyarya.storyapp.model.Story
import com.dickyarya.storyapp.model.StoryResponse
import com.dickyarya.storyapp.preferences.LoginPreference
import com.dickyarya.storyapp.ui.detail.DetailActivity
import com.dickyarya.storyapp.ui.post.PostActivity
import com.dickyarya.storyapp.ui.login.LoginActivity
import kotlin.math.abs

class StoryActivity : AppCompatActivity(){
    private lateinit var binding: ActivityStoryBinding
    private lateinit var preference: LoginPreference
    private lateinit var viewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = LoginPreference(this)

        showLoading(true)

        storyAdapter = StoryAdapter()

        goToDetail()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryViewModel::class.java]

        val token: String = preference.getString(LoginPreference.TOKEN).toString()

        showList(token)

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@StoryActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = storyAdapter
            swipeLayout.setOnRefreshListener {
                showList(token)
                binding.swipeLayout.isRefreshing = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val token: String = preference.getString(LoginPreference.TOKEN).toString()
        showList(token)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       val inflater = menuInflater
       inflater.inflate(R.menu.option_menu, menu)
       return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.loMenu -> {
                preference.logOut()
                moveIntentLogOut()
                finish()
                true
            }
            R.id.asMenu -> {
                moveIntentPostStory()
                true
            }
            R.id.maMenu -> {
                moveIntentMap()
                true
            }
            else -> true
        }
    }

    private fun goToDetail(){
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Story) {
                moveIntentDetail(data)
            }

        })
    }

    private fun moveIntentDetail(data: Story){
        Intent(this@StoryActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.USERNAME, data.name)
            it.putExtra(DetailActivity.DESCRIPTION, data.description)
            it.putExtra(DetailActivity.PHOTO, data.photoUrl)
            it.putExtra(DetailActivity.DATE, data.createdAt)
            it.putExtra(DetailActivity.LAT, data.lat)
            it.putExtra(DetailActivity.LON, data.lon)
            startActivity(it)
        }
    }

    private fun moveIntentMap(){
        Intent(this, MapsActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun moveIntentLogOut(){
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun moveIntentPostStory(){
        Intent(this, PostActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun getStories(token: String){
        viewModel.getStories(token)
    }

    private fun showList(token: String){
        getStories(token)
        viewModel.getListStories().observe(this){
            if (it != null){
                storyAdapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun showToast(text: String){
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar3.visibility = View.VISIBLE
        }else{
            binding.progressBar3.visibility = View.GONE
        }
    }

}