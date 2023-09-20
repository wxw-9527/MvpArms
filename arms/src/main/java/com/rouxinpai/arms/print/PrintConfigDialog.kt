package com.rouxinpai.arms.print

import androidx.lifecycle.LifecycleOwner
import com.kongzue.dialogx.dialogs.BottomDialog
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.dialog.BaseOnBindView
import com.rouxinpai.arms.databinding.PrintConfigDialogBinding
import com.rouxinpai.arms.print.model.TemplateVO
import com.rouxinpai.arms.print.util.PrintUtil
import com.skydoves.powerspinner.DefaultSpinnerAdapter
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/19 14:42
 * desc   :
 */
class PrintConfigDialog(
    private val templateList: List<TemplateVO>,
    private val block: (template: TemplateVO, copies: Int) -> Unit,
) : BaseOnBindView<BottomDialog, PrintConfigDialogBinding>(R.layout.print_config_dialog) {

    companion object {

        /**
         * 展示[PrintConfigDialog]弹窗
         */
        fun show(
            templateList: List<TemplateVO>,
            block: (template: TemplateVO, copies: Int) -> Unit,
        ) {
            val view = PrintConfigDialog(templateList, block)
            BottomDialog.build()
                .setTitle(R.string.print_config__title)
                .setCancelable(false)
                .setCustomView(view)
                .show()
        }
    }

    private var mTemplate: TemplateVO? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 设置按钮监听事件
        dialog
            .setCancelButton(R.string.print_config__cancel)
            .setOkButton(R.string.print_config__ok) { _, _ ->
                // 打印模板
                val template = mTemplate
                if (template == null) {
                    showWarningTip(R.string.print_config__template_hint)
                    return@setOkButton true
                }
                // 份数
                val copies = binding.stepperCopies.getValue()?.toInt()
                if (copies == null) {
                    showWarningTip(R.string.print_config__copies_hint)
                    return@setOkButton true
                }
                //
                block.invoke(template, copies)
                false
            }
        // 绑定适配器
        binding.spinnerTemplate.setSpinnerAdapter(DefaultSpinnerAdapter(binding.spinnerTemplate))
        // 绑定监听事件
        binding.spinnerTemplate.setOnSpinnerOutsideTouchListener { _, _ -> binding.spinnerTemplate.dismiss() }
        binding.spinnerTemplate.setOnSpinnerItemSelectedListener(mOnTemplateItemSelectedListener)
        // 渲染下拉框数据
        binding.spinnerTemplate.setItems(templateList.map { it.name })
        // 设置默认选中
        val template = PrintUtil.getTemplate()
        if (template != null) {
            val index = templateList.indexOf(template)
            binding.spinnerTemplate.selectItemByIndex(index)
        }
        // 初始化份数步进器
        with(binding.stepperCopies) {
            setMinValue(1f)
            setMaxValue(12f)
            setValue(1f)
        }
    }

    // 选中监听
    private val mOnTemplateItemSelectedListener = OnSpinnerItemSelectedListener<String> { oldIndex, _, newIndex, _ ->
        if (newIndex != oldIndex) {
            // 设置选中
            val template = templateList[newIndex]
            mTemplate = template
            // 缓存选中模板
            PrintUtil.setTemplate(template)
        }
    }
}