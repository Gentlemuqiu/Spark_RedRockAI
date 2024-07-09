package com.example.redrockai.module.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentTransaction
import com.example.redrockai.module.message.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _mBinding: FragmentMainBinding? = null
    private val mBinding: FragmentMainBinding
        get() = _mBinding!!
    private val messageFragment by lazy { MessageFragment() }
    private val secondFragment by lazy { SecondFragment() }
    private val thirdFragment by lazy { ThirdFragment() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentMainBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.spinner.adapter = adapter

        mBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> showFragment(messageFragment)
                    1 -> showFragment(secondFragment)
                    2 -> showFragment(thirdFragment)
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        showFragment(messageFragment)

    }


    private fun showFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()

        childFragmentManager.fragments.forEach {
            transaction.hide(it)
        }

        if (!fragment.isAdded) {
            transaction.add(R.id.fragment_container, fragment)
        }

        transaction.show(fragment).commit()
    }
}