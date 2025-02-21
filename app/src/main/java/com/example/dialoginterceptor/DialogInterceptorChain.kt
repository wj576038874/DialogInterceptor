package com.example.dialoginterceptor

import android.content.Context
import java.util.LinkedList

/**
 * Created by wenjie on 2025/02/21.
 */
class DialogInterceptorChain(private val context: Context) : Chain {

    private val interceptors = LinkedList<DialogInterceptor>()

    private var isIntercepted = false

    private var isDialogShowing = false

    fun addInterceptor(interceptor: DialogInterceptor) {
        interceptors.add(interceptor)
        proceed()
    }

    override fun proceed() {
        if (!isIntercepted && !interceptors.isEmpty() && !isDialogShowing) {
            val nextInterceptor = interceptors.poll()
            nextInterceptor?.intercept(this)
            isDialogShowing = true
        }
    }

    override fun intercept() {
        isIntercepted = true
    }

    override fun getContext(): Context {
        return context
    }

    fun dismiss() {
        isDialogShowing = false
    }

    fun clear() {
        interceptors.clear()
    }

}