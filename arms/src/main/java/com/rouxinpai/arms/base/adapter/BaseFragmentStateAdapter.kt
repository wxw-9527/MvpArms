@file:Suppress("unused")

package com.rouxinpai.arms.base.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2021/5/13 16:11
 * desc   :
 */
abstract class BaseFragmentStateAdapter<T> : BaseSimpleFragmentStateAdapter {

    private val mList: List<T>

    constructor(activity: AppCompatActivity, list: List<T>) : super(activity) {
        mList = list
    }

    constructor(fragment: Fragment, list: List<T>) : super(fragment) {
        mList = list
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun createFragment(position: Int): Fragment {
        return createFragment(position, mList[position])
    }

    protected abstract fun createFragment(position: Int, item: T): Fragment
}