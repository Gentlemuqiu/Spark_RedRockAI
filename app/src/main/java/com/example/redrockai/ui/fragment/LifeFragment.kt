package com.example.redrockai.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redrockai.R
import com.example.redrockai.databinding.FragmentClassBinding
import com.example.redrockai.databinding.FragmentLifeBinding

class LifeFragment : Fragment() {

    private lateinit var mBinding: FragmentLifeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLifeBinding.inflate(inflater, container, false)
        return mBinding.root
    }


}