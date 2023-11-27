package com.rouxinpai.demo.feature.demo.dict

import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.demo.databinding.DictActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/24 17:30
 * desc   :
 */
@AndroidEntryPoint
class DictActivity : BaseMvpActivity<DictActivityBinding, DictContract.View, DictPresenter>(),
    DictContract.View