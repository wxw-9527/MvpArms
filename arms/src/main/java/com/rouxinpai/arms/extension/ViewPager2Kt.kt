package com.rouxinpai.arms.extension

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.next() {
    currentItem = ++currentItem
}

fun ViewPager2.previous() {
    currentItem = --currentItem
}