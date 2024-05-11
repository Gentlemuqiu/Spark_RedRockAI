package com.example.redrockai.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redrockai.R
import com.example.redrockai.databinding.ActivityHomeBinding
import com.example.redrockai.databinding.FragmentClassBinding


class ClassFragment : Fragment() {


    private lateinit var mBinding: FragmentClassBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }


}