package com.example.redrockai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redrockai.databinding.FragmentLifeBinding

class LifeFragment : Fragment() {

    private var _mBinding: FragmentLifeBinding? = null
    private val mBinding: FragmentLifeBinding
        get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentLifeBinding.inflate(inflater, container, false)
        return mBinding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}