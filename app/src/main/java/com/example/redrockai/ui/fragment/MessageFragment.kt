package com.example.redrockai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redrockai.databinding.FragmentMessageBinding

class MessageFragment : Fragment() {

    private var _mBinding: FragmentMessageBinding? = null
    private val mBinding: FragmentMessageBinding
        get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentMessageBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}