package com.example.module.life.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.module.life.R
import com.example.module.life.bean.ArticleResponse
import com.example.module.life.ui.Activity.WebActivity

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 22:42.
 * @Description : 生活很难，但总有人在爱你。
 */
class SearchAdapter  : PagingDataAdapter<ArticleResponse.Data.Article, SearchAdapter.SearchViewHolder>(ARTICLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    inner  class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener {
                getItem(absoluteAdapterPosition)?.run {
                    WebActivity.startAction(it.context, link)
                }
            }
        }
        private val title: TextView = itemView.findViewById(R.id.tv_content)
        private val author: TextView = itemView.findViewById(R.id.tv_author)
        private val time: TextView = itemView.findViewById(R.id.tv_time)
        private val category: TextView = itemView.findViewById(R.id.tv_category)

        fun bind(article: ArticleResponse.Data.Article) {
            title.text = article.title
            author.text = article.author
            time.text = article.niceShareDate
            category.text = article.chapterName
        }
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<ArticleResponse.Data.Article>() {
            override fun areItemsTheSame(oldItem: ArticleResponse.Data.Article, newItem: ArticleResponse.Data.Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleResponse.Data.Article, newItem: ArticleResponse.Data.Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}