package com.dickyarya.storyapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dickyarya.storyapp.api.Api
import com.dickyarya.storyapp.model.Story
import com.dickyarya.storyapp.model.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel: ViewModel() {
    val listStories = MutableLiveData<ArrayList<Story>>()

    fun getStories(TOKEN: String){
        Api.getApi().getStories(
            "Bearer $TOKEN"
        ).enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    listStories.postValue(response.body()?.listStory)
                    Log.d("StoryResponse", response.body()?.listStory.toString())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.d("StoryResponse", t.message.toString())
            }

        })
    }

    fun getListStories(): LiveData<ArrayList<Story>>{
        return listStories
    }
}