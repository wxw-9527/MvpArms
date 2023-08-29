package com.rouxinpai.demo.feature.demo.dropdown

import android.os.Bundle
import android.view.View
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.view.DropdownButton
import com.rouxinpai.demo.databinding.DropdownActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/3 16:27
 * desc   :
 */
class DropdownActivity : BaseActivity<DropdownActivityBinding>(),
    DropdownButton.OnItemSelectedListener {

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.dropdownMenu1.setOnItemSelectedListener(this)
        binding.dropdownMenu1.setList(
            listOf(
                DropdownEntity("1", "选项000"),
                DropdownEntity("1", "选项111"),
                DropdownEntity("2", "选项222"),
                DropdownEntity("3", "选项333"),
                DropdownEntity("4", "选项444"),
                DropdownEntity("5", "选项555"),
                DropdownEntity("6", "选项666"),
                DropdownEntity("7", "选项777"),
                DropdownEntity("7", "选项888"),
                DropdownEntity("7", "选项999"),
                DropdownEntity("7", "选项10"),
                DropdownEntity("7", "选项11"),
                DropdownEntity("7", "选项12"),
                DropdownEntity("7", "选项13"),
                DropdownEntity("7", "选项14"),
            )
        )


        binding.dropdownMenu2.setOnItemSelectedListener(this)
        binding.dropdownMenu2.setList(
            listOf(
                DropdownEntity("1", "选项111"),
                DropdownEntity("2", "选项222"),
                DropdownEntity("3", "选项333"),
                DropdownEntity("4", "选项444"),
                DropdownEntity("5", "选项555"),
                DropdownEntity("6", "选项666"),
                DropdownEntity("7", "选项777"),
            )
        )

        binding.btnClear.setOnClickListener {
            binding.dropdownMenu2.clear()
        }
    }

    override fun onItemSelected(view: View, position: Int, item: DropdownButton.IDropdownEntity) {
        val entity = item as? DropdownEntity ?: return
        showSuccessTip(entity.name)
    }
}