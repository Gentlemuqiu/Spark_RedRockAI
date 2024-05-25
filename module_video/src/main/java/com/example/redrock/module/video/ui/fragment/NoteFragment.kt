package com.example.redrock.module.video.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.model.play.Adapter.TopAdapter
import com.example.model.play.ViewModel.RelatedViewModel
import com.example.redrock.module.video.R
import com.example.redrock.module.video.databinding.ActivityMainBinding
import com.example.redrock.module.video.databinding.FragmentNoteBinding


class NoteFragment : Fragment() {
    private val mBinding: FragmentNoteBinding by lazy {
        FragmentNoteBinding.inflate(layoutInflater)
    }
    private val relatedViewModel by lazy { ViewModelProvider(this)[RelatedViewModel::class.java] }
    private lateinit var topAdapter: TopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = 8810
        relatedViewModel.getRelated(id)
        topAdapter = TopAdapter()
        relatedViewModel.relatedData.observe(viewLifecycleOwner) {
            val list = it.itemList.filter { element ->
                element.type == "videoSmallCard"
            }

            Log.d("hui", "onViewCreated: ${it}")
            topAdapter.submitList(list)
        }

        mBinding.rvPlay.layoutManager = LinearLayoutManager(context)
        mBinding.rvPlay.adapter = topAdapter
    }
}