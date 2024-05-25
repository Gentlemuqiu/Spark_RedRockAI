package com.example.model.play.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redrock.module.video.Bean.Related
import com.example.redrock.module.video.ui.activity.PlayActivity
import com.example.redrock.module.video.R
import com.example.redrockai.lib.utils.formatNumberToTime


class TopAdapter() :
    ListAdapter<Related.Item, TopAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Related.Item>() {
            override fun areItemsTheSame(
                oldItem: Related.Item,
                newItem: Related.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Related.Item,
                newItem: Related.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val coverIV: ImageView
        val timeTV: TextView
        val titleTV: TextView
        val categoryTV: TextView
        val relatedCL: ConstraintLayout

        init {
            view.run {
                coverIV = findViewById(R.id.iv_cover)
                timeTV = findViewById(R.id.tv_time)
                titleTV = findViewById(R.id.tv_title)
                categoryTV = findViewById(R.id.tv_category)
                relatedCL = findViewById(R.id.cl_related)
            }
            relatedCL.setOnClickListener {
                val intent = Intent(view.context, PlayActivity::class.java)
                getItem(absoluteAdapterPosition).run {
                    intent.putExtra("playUrl", data.playUrl)
                    intent.putExtra("title", data.title)
                    intent.putExtra("description", data.description)
                    intent.putExtra("category", data.category)
                    intent.putExtra("shareCount", data.consumption.shareCount)
                    intent.putExtra("likeCount", data.consumption.realCollectionCount)
                    intent.putExtra("commentCount", data.consumption.replyCount)
                    intent.putExtra("id", data.id)
                }
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.related_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            getItem(position).run {
                Glide.with(itemView).load(data.cover.detail).into(holder.coverIV)
                titleTV.text = data.title
                timeTV.text = formatNumberToTime(data.duration)
                categoryTV.text = data.category
            }
        }
    }
}
