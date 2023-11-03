package com.rouxinpai.demo.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.demo.databinding.MainActivityBinding
import com.rouxinpai.demo.databinding.MainRecycleItemBinding
import com.rouxinpai.demo.model.entity.main.MenuEnum
import com.rouxinpai.demo.model.entity.main.MenuEnum.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@BarcodeScanningReceiverEnabled
class MainActivity : BaseActivity<MainActivityBinding>(), BaseQuickAdapter.OnItemClickListener<MenuEnum> {

    companion object {

        /**
         * 启动[MainActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    // 菜单列表适配器
    private val mMenuAdapter = MenuAdapter()

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定适配器
        binding.recyclerView.adapter = mMenuAdapter
        // 添加分割线
        dividerBuilder()
            .asSpace()
            .size(8, TypedValue.COMPLEX_UNIT_DIP)
            .showFirstDivider()
            .showSideDividers()
            .showLastDivider()
            .build()
            .addTo(binding.recyclerView)
        // 填充数据
        mMenuAdapter.submitList(values().toList())
        // 绑定监听事件
        mMenuAdapter.setOnItemClickListener(this)
    }

    override fun onClick(adapter: BaseQuickAdapter<MenuEnum, *>, view: View, position: Int) {
        // 获取点击的菜单项
        val item = adapter.getItem(position) ?: return
        // 跳转到对应的页面
        val intent = Intent(this, item.clazz)
        startActivity(intent)
    }

    /**
     * 菜单列表适配器
     */
    private class MenuAdapter : BaseVbAdapter<MainRecycleItemBinding, MenuEnum>() {

        override fun onBindView(binding: MainRecycleItemBinding, position: Int, item: MenuEnum) {
            binding.tvText.text = item.value
        }
    }
}