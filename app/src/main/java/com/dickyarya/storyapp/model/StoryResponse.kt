package com.dickyarya.storyapp.model

import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@field:SerializedName("listStory")
	val listStory: ArrayList<Story>
)

