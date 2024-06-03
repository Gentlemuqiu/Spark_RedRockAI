package com.example.module.life.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module.life.R

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 10:06.
 * @Description : 生活很难，但总有人在爱你。
 */
class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(itemView: View, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val textLoad: TextView = itemView.findViewById(R.id.load)

        private val retryButton: Button = itemView.findViewById(R.id.retry_button)
        private val errorMsg: TextView = itemView.findViewById(R.id.error_msg)
        private val retryLL: LinearLayout=itemView.findViewById(R.id.ll_retry)
        init {
            retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            textLoad.isVisible=loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
            retryLL.orientation = if(loadState is LoadState.Error){
                LinearLayout.VERTICAL
            }else  {
                LinearLayout.HORIZONTAL
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading, parent, false)
        return LoadStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
