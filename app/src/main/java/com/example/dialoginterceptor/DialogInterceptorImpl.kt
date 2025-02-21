package com.example.dialoginterceptor

import androidx.appcompat.app.AlertDialog

/**
 * Created by wenjie on 2025/02/21.
 */
class DialogInterceptorImpl(private val title: String, private val interceptNext: Boolean = false) :
    DialogInterceptor {

    override fun intercept( chain: DialogInterceptorChain) {
        AlertDialog.Builder(chain.getContext()).setTitle(title).setMessage("message")
            .setPositiveButton("ok") { _, _ ->

            }.setNeutralButton("cancel") { _, _ ->

            }.setOnDismissListener {
                chain.dismiss()
                if (interceptNext) {
                    chain.intercept()
                } else {
                    chain.proceed()
                }
            }.show()
    }
}