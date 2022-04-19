package com.dickyarya.storyapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dickyarya.storyapp.databinding.ItemStoryBinding
import com.dickyarya.storyapp.model.Story

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val listStory = ArrayList<Story>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(stories : ArrayList<Story>){
        listStory.clear()
        listStory.addAll(stories)
        notifyDataSetChanged()
    }
    inner class StoryViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(story: Story){
           binding.apply {
               Glide.with(itemView)
                   .load(story.photoUrl)
                   .into(ivImage)
               tvName.text = story.name
               tvDate.text = story.createdAt

               root.setOnClickListener {
                   onItemClickCallback.onItemClicked(story)
               }
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback{
        fun onItemClicked(data: Story)
    }
}