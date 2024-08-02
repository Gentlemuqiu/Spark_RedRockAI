package com.example.redrock.module.video.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.model.play.Adapter.TopAdapter
import com.example.model.play.ViewModel.RelatedViewModel
import com.example.redrock.module.video.ViewModel.IdViewModel
import com.example.redrock.module.video.databinding.FragmentRecommendBinding


class RecommendFragment : Fragment() {
    private val mBinding: FragmentRecommendBinding by lazy {
        FragmentRecommendBinding.inflate(layoutInflater)
    }
    private val relatedViewModel by lazy { ViewModelProvider(this)[RelatedViewModel::class.java] }
    private lateinit var idViewModel: IdViewModel

    private lateinit var topAdapter: TopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idViewModel = ViewModelProvider(requireActivity()).get(IdViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idViewModel.id.observe(viewLifecycleOwner) {
            relatedViewModel.getRelated(it)
        }
        topAdapter = TopAdapter()
        relatedViewModel.relatedData.observe(viewLifecycleOwner) {
            topAdapter.submitList(it.data)
        }

        mBinding.rvPlay.layoutManager = LinearLayoutManager(context)
        mBinding.rvPlay.adapter = topAdapter
    }
}