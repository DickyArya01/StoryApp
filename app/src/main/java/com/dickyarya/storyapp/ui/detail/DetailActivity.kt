package com.dickyarya.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dickyarya.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        val username = intent.getStringExtra(USERNAME)
        val desc= intent.getStringExtra(DESCRIPTION)
        val photo = intent.getStringExtra(PHOTO)
        val date= intent.getStringExtra(DATE)
       binding.apply {
          Glide.with(this@DetailActivity)
              .load(photo)
              .into(ivDetailImage)

           tvDetailName.text = username
           tvDetailDescription.text = desc
           tvDetailDate.text = date
       }

    }

    private fun setActionBar(){
        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object{
        const val USERNAME = "name"
        const val DESCRIPTION= "desc"
        const val PHOTO= "photo"
        const val DATE= "date"
    }
}