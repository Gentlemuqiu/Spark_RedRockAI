package com.example.module.life.Adapter

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-02 20:46.
 * @Description : 生活很难，但总有人在爱你。
 */
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.PagingDataAdapter
import com.example.module.life.R
import com.example.module.life.bean.ArticleResponse

class ArticleAdapter : PagingDataAdapter<ArticleResponse.Data.Article, ArticleAdapter.ViewHolder>(ARTICLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = getItem(position)
        if (itemData != null) {
            holder.bind(itemData)
        }

    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {

            }
        }

        private val itemAuthor: TextView = view.findViewById(R.id.tv_author)
        private val itemTime: TextView = view.findViewById(R.id.tv_time)
        private val itemTitle: TextView = view.findViewById(R.id.tv_content)
        private val itemCategory: TextView = view.findViewById(R.id.tv_category)


        fun bind(itemData:ArticleResponse.Data.Article ) {
            if(itemData.author!=""){
                itemAuthor.text=itemData.author
            }else {
                itemAuthor.text="佚名"
            }
            itemCategory.text=itemData.chapterName
            itemTime.text=itemData.niceShareDate
            itemTitle.text=itemData.title

        }


    }


    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<ArticleResponse.Data.Article>() {
            override fun areItemsTheSame(oldItem: ArticleResponse.Data.Article, newItem: ArticleResponse.Data.Article): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(oldItem: ArticleResponse.Data.Article, newItem: ArticleResponse.Data.Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}
