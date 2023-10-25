package com.rouxinpai.arms.print.factory

import com.rouxinpai.arms.print.model.BrandEnum.*
import com.rouxinpai.arms.print.util.PrintUtil

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/24 14:57
 * desc   :
 */
object PrinterFactory {

    fun createPrinter(): Printer {
        return when (PrintUtil.getBrandEnum()) {
            HCCTG -> HcctgPrinter.getInstance()
            HPRT -> HrptPrinter.getInstance()
        }
    }
}