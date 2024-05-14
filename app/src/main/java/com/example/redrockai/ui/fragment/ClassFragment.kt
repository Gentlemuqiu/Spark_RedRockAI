package com.example.redrockai.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redrockai.R
import com.example.redrockai.databinding.ActivityHomeBinding
import com.example.redrockai.databinding.FragmentClassBinding
import com.example.redrockai.databinding.FragmentMineBinding


class ClassFragment : Fragment() {


    private var _mBinding: FragmentClassBinding? = null
    private val mBinding: FragmentClassBinding
        get() = _mBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}