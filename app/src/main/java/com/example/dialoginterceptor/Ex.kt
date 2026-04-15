package com.example.dialoginterceptor

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by wenjie on 2026/04/15.
 */
suspend fun Activity.showAwaitDialog(title: String) {
    suspendCancellableCoroutine { continuation ->
        val dialog = AlertDialog.Builder(this).setTitle(title).setMessage("message")
            .setPositiveButton("ok") { _, _ ->

            }.setNeutralButton("cancel") { _, _ ->
                continuation.cancel()//可以取消协程 中断后续的dialog弹出
            }.setOnDismissListener {
                continuation.resume(Unit)
            }.create()
        dialog.show()
        continuation.invokeOnCancellation {
            dialog.dismiss()
        }
    }
}