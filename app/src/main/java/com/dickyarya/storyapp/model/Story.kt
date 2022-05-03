package com.dickyarya.storyapp.model

import com.google.gson.annotations.SerializedName

data class Story(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: String
)
