package com.rouxinpai.arms.print

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.PrinterSettingActivityBinding
import com.rouxinpai.arms.databinding.PrinterSettingRecycleItemBinding
import com.rouxinpai.arms.print.model.BrandEnum
import com.rouxinpai.arms.print.util.PrintUtil

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/20 15:20
 * desc   : 打印机设置页
 */
class PrinterSettingActivity : BaseActivity<PrinterSettingActivityBinding>(), OnItemClickListener {

    companion object {

        /**
         * 启动[PrinterSettingActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PrinterSettingActivity::class.java)
            context.startActivity(starter)
        }
    }

    // 打印机品牌列表适配器实例
    private val mBrandAdapter = BrandAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定适配器
        binding.rvBrand.adapter = mBrandAdapter
        // 填充列表数据
        mBrandAdapter.setBrandEnum(PrintUtil.getBrandEnum())
        mBrandAdapter.setList(BrandEnum.values().toList())
        // 绑定监听事件
        mBrandAdapter.setOnItemClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = adapter.getItemOrNull(position) as? BrandEnum ?: return
        // 记录选中项
        PrintUtil.setBrandEnum(item)
        // 选中
        mBrandAdapter.setBrandEnum(item)
        mBrandAdapter.notifyDataSetChanged()
        // 提示
        showSuccessTip(R.string.printer_setting__switch_successful)
    }

    /**
     * 打印机品牌列表适配器
     */
    private class BrandAdapter : BaseVbAdapter<PrinterSettingRecycleItemBinding, BrandEnum>() {

        // 已选中品牌
        private var mSelectedBrandEnum: BrandEnum? = null

        /**
         * 设置选中品牌
         */
        fun setBrandEnum(brandEnum: BrandEnum) {
            mSelectedBrandEnum = brandEnum
        }

        override fun onBindView(
            binding: PrinterSettingRecycleItemBinding,
            position: Int,
            item: BrandEnum,
        ) {
            // 选中
            binding.radioButton.isChecked = (mSelectedBrandEnum == item)
            // 品牌名称
            binding.tvText.setText(item.brandNameResId)
        }
    }
}