package com.example.redrockai.module.schoolroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redrockai.module.schoolroom.R
import com.example.redrockai.module.schoolroom.helper.date.DateTransformer.formatTimestamp
import com.example.redrockai.module.schoolroom.helper.room.bean.HistoryRecord

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 11:15
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */

class PastCourseRvAdapter : ListAdapter<HistoryRecord, PastCourseRvAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<HistoryRecord>() {
    override fun areItemsTheSame(oldItem: HistoryRecord, newItem: HistoryRecord) =
        oldItem.newsId == newItem.newsId

    override fun areContentsTheSame(oldItem: HistoryRecord, newItem: HistoryRecord) =
        oldItem == newItem
}
) {

    private var mItemClick: (() -> Unit)? = null

    fun setOnClassItemClickListener(cl: (() -> Unit)) {
        mItemClick = cl
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_rv_past, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = getItem(position)
        holder.bind(itemData)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                mItemClick?.invoke()
            }
        }

        private val playImage: ImageView = view.findViewById(R.id.item_history_image)
        private val itemTitle: TextView = view.findViewById(R.id.item_history_title)
        private val itemDesc: TextView = view.findViewById(R.id.item_history_des)
        private val itemLastSeeTime: TextView = view.findViewById(R.id.item_history_time)


        fun bind(itemData: HistoryRecord) {

            itemTitle.text = itemData.title
            itemDesc.text = itemData.description
            itemLastSeeTime.text = "学习时间：".plus(formatTimestamp(itemData.timestamp))

            Glide.with(itemView).load(itemData.coverDetail).into(playImage)
        }


    }
}
