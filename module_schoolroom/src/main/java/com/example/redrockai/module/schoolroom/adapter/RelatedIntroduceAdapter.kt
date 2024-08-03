package com.example.redrockai.module.schoolroom.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redrockai.lib.utils.formatNumberToTime
import com.example.redrockai.module.schoolroom.R
import com.example.redrockai.module.schoolroom.bean.RelatedCategoryBean

class RelatedIntroduceAdapter :
    ListAdapter<RelatedCategoryBean.Data, RelatedIntroduceAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<RelatedCategoryBean.Data>() {
        override fun areItemsTheSame(
            oldItem: RelatedCategoryBean.Data, newItem: RelatedCategoryBean.Data
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

    private var mItemClick: ((RelatedCategoryBean.Data) -> Unit)? = null

    fun setOnClassItemClickListener(cl: ((RelatedCategoryBean.Data) -> Unit)) {
        mItemClick = cl
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_course_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = getItem(position)
        holder.bind(itemData)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        init {
            view.setOnClickListener {
                mItemClick?.invoke(getItem(adapterPosition))
            }
        }

        private val playImage: ImageView = view.findViewById(R.id.banner_image)
        private val itemTitle: TextView = view.findViewById(R.id.course_item_title)
        private val itemDesc: TextView = view.findViewById(R.id.course_item_descr)


        fun bind(itemData: RelatedCategoryBean.Data) {
            Log.d("LogTest", "测试数据${itemData}")


            itemTitle.text = itemData.title
            itemDesc.text = itemData.desc
            Glide.with(itemView).load(itemData.coverUrl).into(playImage)
        }


    }
}
