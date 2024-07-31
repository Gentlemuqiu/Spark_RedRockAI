package com.example.module.life.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module.life.databinding.SearchItemHistoryBinding

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 11:23.
 * @Description : 生活很难，但总有人在爱你。
 */
class SearchHistoryAdapter : ListAdapter<String, SearchHistoryAdapter.MyHolder>(myDiffUtil) {
    companion object {
        val myDiffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    // 删除状态，如果删除状态是true，那么就显示×号
    var isDeleteStatus = false

    private var mOnClickItem: ((content: String) -> Unit)? = null

    fun setOnClickItem(onClickItem: (content: String) -> Unit) {
        this.mOnClickItem = onClickItem
    }

    private var mOnClickCha: ((content: String) -> Unit)? = null

    fun setOnClickCha(mOnClickCha: (content: String) -> Unit) {
        this.mOnClickCha = mOnClickCha
    }

    inner class MyHolder(val binding: SearchItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.searchItemHistoryTvHistory.setOnClickListener {
                mOnClickItem?.invoke(getItem(bindingAdapterPosition))
            }
            binding.searchItemHistoryImgCha.setOnClickListener {
                mOnClickCha?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            SearchItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.binding.searchItemHistoryTvHistory.text = item
            if (isDeleteStatus){
                holder.binding.searchItemHistoryImgCha.visibility = View.VISIBLE
            }else{
                holder.binding.searchItemHistoryImgCha.visibility = View.GONE
            }
        }
    }
}