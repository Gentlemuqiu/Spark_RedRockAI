package com.example.redrockai.module.schoolroom.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redrockai.module.schoolroom.R
import com.example.redrockai.module.schoolroom.ui.fragment.Bean.RelatedCategoryBean

class RelatedIntroduceAdapter() :
    ListAdapter<RelatedCategoryBean.Item, RelatedIntroduceAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<RelatedCategoryBean.Item>() {
            override fun areItemsTheSame(
                oldItem: RelatedCategoryBean.Item,
                newItem: RelatedCategoryBean.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RelatedCategoryBean.Item,
                newItem: RelatedCategoryBean.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playImage: ImageView

        init {
            view.run {
                playImage = findViewById(R.id.banner_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_course_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            getItem(position).run {
                Glide.with(itemView).load(data.content.data.cover.detail).into(playImage)
            }
        }
    }
}
