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
import com.example.redrock.module.video.Bean.RelatedCategoryBean
import com.example.redrock.module.video.ui.activity.PlayActivity
import com.example.redrock.module.video.R
import com.example.redrockai.lib.utils.formatNumberToTime
import com.example.redrockai.lib.utils.room.bean.HistoryRecord
import com.example.redrockai.lib.utils.room.dao.HistoryRecordDao
import com.example.redrockai.lib.utils.room.db.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TopAdapter() :
    ListAdapter<RelatedCategoryBean.Data, TopAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<RelatedCategoryBean.Data>() {
            override fun areItemsTheSame(
                oldItem: RelatedCategoryBean.Data,
                newItem: RelatedCategoryBean.Data
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RelatedCategoryBean.Data,
                newItem: RelatedCategoryBean.Data
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    private lateinit var historyRecordDao: HistoryRecordDao

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val coverIV: ImageView
        val timeTV: TextView
        val titleTV: TextView
        val categoryTV: TextView
        val relatedCL: ConstraintLayout

        init {
            historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()

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
                  /*  val record = HistoryRecord(
                        newsId = data.id,
                        title = data.title,
                        timestamp = System.currentTimeMillis(),
                        playerUrl = data.playUrl,
                        description = data.description,
                        coverDetail = data.cover.detail,
                        category = data.category,
                        shareCount = data.consumption.shareCount,
                        likeCount = data.consumption.realCollectionCount
                        commentCount = data.consumption.replyCount

                    )*/
                    /*GlobalScope.launch {
                        historyRecordDao.insertOrUpdate(record)
                    }*/
                  /*  intent.putExtra("playUrl", data.playUrl)
                    intent.putExtra("title", data.title)
                    intent.putExtra("description", data.description)
                    intent.putExtra("category", data.category)
                    intent.putExtra("id", data.id)*/
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
                Glide.with(itemView).load(coverUrl).into(holder.coverIV)
                titleTV.text = title
            }
        }
    }
}
