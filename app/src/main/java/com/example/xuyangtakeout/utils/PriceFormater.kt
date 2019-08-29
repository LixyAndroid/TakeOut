package com.example.xuyangtakeout.utils

import java.text.NumberFormat


object PriceFormater {

    fun format(countPrice: Float): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        return format.format(countPrice.toDouble())
    }
}
