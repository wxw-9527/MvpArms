package com.rouxinpai.demo.feature.demo.search

import android.os.Bundle
import android.view.View
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.view.SearchEdittext
import com.rouxinpai.demo.databinding.SearchEdittextDemoActivityBinding
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/18 10:58
 * desc   :
 */
class SearchEdittextDemoActivity : BaseActivity<SearchEdittextDemoActivityBinding>(),
    SearchEdittext.OnTextChangedListener {

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.searchEditText.setOnTextChangedListener(this)
        binding.btnSetText.setOnClickListener {
            binding.searchEditText.setText("11qwe")
        }
    }

    override fun onTextChanged(v: View, text: String?) {
        Timber.tag("SearchEdittext").d("text: $text")
    }
}