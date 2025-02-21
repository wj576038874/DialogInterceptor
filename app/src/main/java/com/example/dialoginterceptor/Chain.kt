package com.example.dialoginterceptor

import android.content.Context

/**
 * Created by wenjie on 2025/02/21.
 */
interface Chain {
    fun proceed()
    fun intercept()
    fun getContext(): Context
}