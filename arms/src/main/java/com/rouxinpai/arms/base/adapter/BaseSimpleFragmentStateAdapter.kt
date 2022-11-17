package com.rouxinpai.arms.base.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2021/5/13 16:05
 * desc   :
 */
abstract class BaseSimpleFragmentStateAdapter : FragmentStateAdapter {

    constructor(activity: AppCompatActivity) : super(activity)

    constructor(fragment: Fragment) : super(fragment)
}