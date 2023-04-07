package com.iedrania.distoring.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iedrania.distoring.data.model.Story
import com.iedrania.distoring.databinding.ItemStoryBinding
import com.iedrania.distoring.ui.detail.DetailActivity

class StoryAdapter(private val listStory: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.binding.tvItemName.text = story.name
        Glide.with(holder.itemView).load(story.photoUrl).into(holder.binding.ivItemPhoto)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("extra_story", story)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = listStory.size

    class ViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)
}