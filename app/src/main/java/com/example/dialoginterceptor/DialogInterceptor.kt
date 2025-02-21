package com.example.dialoginterceptor


/**
 * Created by wenjie on 2025/02/21.
 */
interface DialogInterceptor {
    fun intercept(chain: DialogInterceptorChain)
}