package com.lhr.notes_kotlin.util

import android.content.Context

/**
 * 生成創建檔案的隨機16碼number。
 */
fun generateRandomNumber(): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..16)
        .map { charset.random() }
        .joinToString("")
}

/**
 * 將dp轉換為像素
 * @param context
 * @param widthInDp 指定的dp
 */
fun dpToPixel(context: Context, widthInDp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (widthInDp * density).toInt()
}

