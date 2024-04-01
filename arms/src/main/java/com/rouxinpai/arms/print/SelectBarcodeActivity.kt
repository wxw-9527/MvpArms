package com.rouxinpai.arms.print

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.arrayMapOf
import androidx.core.os.bundleOf
import com.chad.library.adapter4.BaseQuickAdapter
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.SelectBarcodeActivityBinding
import com.rouxinpai.arms.databinding.SelectBarcodeRecycleBarcodeItemBinding
import com.rouxinpai.arms.databinding.SelectBarcodeRecycleMaterialItemBinding
import com.rouxinpai.arms.print.model.BarcodeVO
import com.rouxinpai.arms.print.model.MaterialVO
import com.rouxinpai.arms.view.OffsetDecoration
import com.rouxinpai.stepper.OnValueChangeListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/4/1 10:23
 * desc   :
 */
@AndroidEntryPoint
class SelectBarcodeActivity :
    BaseMvpActivity<SelectBarcodeActivityBinding, SelectBarcodeContract.View, SelectBarcodePresenter>(),
    SelectBarcodeContract.View,
    BaseQuickAdapter.OnItemChildClickListener<MaterialVO>,
    View.OnClickListener {

    companion object {

        // 参数传递标志
        private const val ARG_BARCODE_LIST = "arg_barcode_list" // 条码列表

        /**
         * 启动[SelectBarcodeActivity]页
         */
        @JvmStatic
        fun start(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            barcodeList: List<String>,
        ) {
            val starter = Intent(context, SelectBarcodeActivity::class.java).apply {
                val bundle = bundleOf(ARG_BARCODE_LIST to barcodeList)
                putExtras(bundle)
            }
            launcher.launch(starter)
        }

        /**
         * 启动[SelectBarcodeActivity]页
         */
        @JvmStatic
        fun start(context: Context, barcodeList: List<String>) {
            val starter = Intent(context, SelectBarcodeActivity::class.java).apply {
                val bundle = bundleOf(ARG_BARCODE_LIST to barcodeList)
                putExtras(bundle)
            }
            context.startActivity(starter)
        }
    }

    // 条码列表
    private var mBarcodeList: List<String> = emptyList()

    // 物料列表适配器实例
    private val mMaterialAdapter = MaterialAdapter()

    override val stateLayout: View
        get() = binding.rvMaterial

    override fun onParseData(bundle: Bundle) {
        super.onParseData(bundle)
        mBarcodeList = bundle.getStringArrayList(ARG_BARCODE_LIST).orEmpty()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定适配器
        binding.rvMaterial.adapter = mMaterialAdapter
        // 添加分割线
        dividerBuilder().asSpace()
            .size(10, TypedValue.COMPLEX_UNIT_DIP)
            .showFirstDivider()
            .showSideDividers()
            .showLastDivider()
            .build()
            .addTo(binding.rvMaterial)
        binding.rvMaterial.addItemDecoration(OffsetDecoration(64f, TypedValue.COMPLEX_UNIT_DIP))
        // 添加监听事件
        binding.btnPrint.setOnClickListener(this)
        mMaterialAdapter.addOnItemChildClickListener(R.id.cb_select_all, this)
        // 获取数据
        presenter.listBarcodeInfos(mBarcodeList)
    }

    override fun showMaterialList(list: List<MaterialVO>) {
        // 更新适配器数据
        mMaterialAdapter.submitList(list)
        // 显示打印按钮
        if (list.isNotEmpty()) {
            binding.btnPrint.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<MaterialVO, *>, view: View, position: Int) {
        val item = adapter.getItem(position) ?: return
        val result = !item.isChecked
        item.isChecked = result
        item.barcodeList.forEach { it.isChecked = result }
        mMaterialAdapter.refreshSelectAllCheckBox(position)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnPrint.id -> onPrintClick()
        }
    }

    // 打印
    private fun onPrintClick() {
        // 获取选中的条码列表
        val selectedBarcodeList = arrayListOf<BarcodeVO>()
        mMaterialAdapter.items.forEach { material ->
            material.barcodeList.forEach { barcode ->
                if (barcode.isChecked) {
                    selectedBarcodeList.add(barcode)
                }
            }
        }
        // 未选中条码
        if (selectedBarcodeList.isEmpty()) {
            showWarningTip(R.string.select_barcode__unselected_barcode)
            return
        }
        // 未填写打印份数
        val missCopiesBarcode = selectedBarcodeList.find { barcodeVo ->
            val copies = barcodeVo.copies
            copies == null || copies < 1
        }
        if (missCopiesBarcode != null) {
            showWarningTip(
                getString(R.string.select_barcode__miss_copies, missCopiesBarcode.barcode)
            )
            return
        }
        // 生成打印数据
        val printDataList = arrayListOf<String>()
        selectedBarcodeList.forEach { barcodeVo ->
            val copies = barcodeVo.copies?.toInt() ?: 1
            for (i in 0 until copies) {
                printDataList.add(barcodeVo.barcode)
            }
        }
        // 打印
        PrintActivity.start(this, mLauncher, printDataList, true)
    }

    private val mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

    }

    /**
     * 物料列表适配器
     */
    private class MaterialAdapter :
        BaseVbAdapter<SelectBarcodeRecycleMaterialItemBinding, MaterialVO>() {

        companion object {
            //
            private const val REFRESH_SELECT_ALL_CHECKBOX = 12
            private const val REFRESH_CHECKBOX = 14
        }

        private val mChildAdapterMap = arrayMapOf<Int, BarcodeAdapter>()

        /**
         * 刷新全选选中框的选中效果
         */
        fun refreshSelectAllCheckBox(position: Int) {
            notifyItemChanged(position, REFRESH_SELECT_ALL_CHECKBOX)
        }

        /**
         * 刷新选中框的选中效果
         */
        fun refreshCheckBox(position: Int) {
            notifyItemChanged(position, REFRESH_CHECKBOX)
        }

        override fun onBindView(
            binding: SelectBarcodeRecycleMaterialItemBinding,
            position: Int,
            item: MaterialVO,
        ) {
            // 全选
            binding.cbSelectAll.isChecked = item.isChecked
            // 物料编码
            binding.tvMaterialCode.text = item.code
            // 物料名称
            binding.tvMaterialName.text = item.name
            // 供应商
            binding.tvSupplierName.text = item.supplerName
            // 条码列表
            var adapter = mChildAdapterMap[position]
            if (adapter == null) {
                // 实例化适配器
                adapter = BarcodeAdapter().apply {
                    addOnItemChildClickListener(R.id.cb_barcode) { childAdapter, _, childPosition ->
                        if (childAdapter is BarcodeAdapter) {
                            // 更新条码选中状态
                            val barcodeVo = childAdapter.getItem(childPosition)
                            if (barcodeVo != null) {
                                //
                                barcodeVo.isChecked = !barcodeVo.isChecked
                                childAdapter.refreshCheckBox(childPosition)
                                //
                                item.isChecked = item.barcodeList.all { it.isChecked }
                                this@MaterialAdapter.refreshCheckBox(position)
                            }
                        }
                    }
                }
                mChildAdapterMap[position] = adapter
                // 添加分割线
                context.dividerBuilder()
                    .size(1, TypedValue.COMPLEX_UNIT_DIP)
                    .colorRes(R.color.select_barcode__divider_color)
                    .build()
                    .addTo(binding.rvBarcode)
            }
            binding.rvBarcode.adapter = adapter
            adapter.submitList(item.barcodeList)
        }

        override fun onBindView(
            binding: SelectBarcodeRecycleMaterialItemBinding,
            position: Int,
            item: MaterialVO,
            payloads: List<Any>,
        ) {
            super.onBindView(binding, position, item, payloads)
            payloads.forEach { payload ->
                when (payload) {
                    REFRESH_SELECT_ALL_CHECKBOX -> {
                        // 全选
                        binding.cbSelectAll.isChecked = item.isChecked
                        // 子列表
                        item.barcodeList.forEachIndexed { index, _ ->
                            mChildAdapterMap[position]?.refreshCheckBox(index)
                        }
                    }
                    REFRESH_CHECKBOX -> {
                        // 全选
                        binding.cbSelectAll.isChecked = item.isChecked
                    }
                }
            }
        }
    }

    /**
     * 条码列表适配器
     */
    private class BarcodeAdapter :
        BaseVbAdapter<SelectBarcodeRecycleBarcodeItemBinding, BarcodeVO>() {

        companion object {
            // 刷新选中效果
            private const val REFRESH_CHECK_BOX = 22
        }

        /**
         * 刷新选中框的选中效果
         */
        fun refreshCheckBox(position: Int) {
            notifyItemChanged(position, REFRESH_CHECK_BOX)
        }

        override fun onBindView(
            binding: SelectBarcodeRecycleBarcodeItemBinding,
            position: Int,
            item: BarcodeVO,
        ) {
            // 条码
            binding.cbBarcode.text = item.barcode
            binding.cbBarcode.isChecked = item.isChecked
            // 打印份数
            with(binding.stepperCopies) {
                setMinValue(1f)
                setMaxValue(12f)
                var listener = tag
                if (listener != null && listener is OnValueChangeListener) {
                    removeListener(listener)
                }
                setValue(item.copies)
                listener = object : OnValueChangeListener {
                    override fun onValueChanged(view: View, value: Float?) {
                        item.copies = value
                    }
                }
                addListener(listener)
                tag = listener
            }
        }

        override fun onBindView(
            binding: SelectBarcodeRecycleBarcodeItemBinding,
            position: Int,
            item: BarcodeVO,
            payloads: List<Any>,
        ) {
            super.onBindView(binding, position, item, payloads)
            payloads.forEach { payload ->
                when (payload) {
                    REFRESH_CHECK_BOX -> {
                        binding.cbBarcode.isChecked = item.isChecked
                    }
                }
            }
        }
    }
}