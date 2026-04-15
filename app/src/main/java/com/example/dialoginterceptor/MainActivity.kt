package com.example.dialoginterceptor

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val dialogInterceptorChain = DialogInterceptorChain(this)

    private val dialogEventFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST // 缓冲满时可选择丢弃最早或挂起
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //shared flow 方式
        lifecycleScope.launch {
            dialogEventFlow.collect {
                showAwaitDialog(it)
            }
        }

        findViewById<Button>(R.id.button_show_dialog).setOnClickListener {
//            lifecycleScope.launch {
//                showAwaitDialog("1111")
//                showAwaitDialog("2222")
//                showAwaitDialog("33333")
//            }

            //flow 方式
            lifecycleScope.launch {
                val flow1 = flow {
                    delay(2000)
                    emit("11111")
                }
                val flow2 = flow {
                    delay(1000)
                    emit("22222")
                }
                val flow3 = flow {
                    delay(2700)
                    emit("33333")
                }
                merge(flow1, flow2, flow3).collect {
                    showAwaitDialog(it)
                }
            }

            //channel 方式
//            lifecycleScope.launch {
//                val channel = Channel<String>()
//
//                launch {
//                    delay(2000)
//                    channel.send("11111")
//                }
//                launch {
//                    delay(1000)
//                    channel.send("2222")
//                }
//                launch {
//                    delay(3000)
//                    channel.send("3333")
//                }
//
//                channel.consumeEach {
//                    showAwaitDialog(it)
//                }
//
////                repeat(3){
////                    val it = channel.receive()
////                    showAwaitDialog(it)
////                }
////                // 此时通道内没有元素了，可以安全地 cancel 通道（或不处理，等待 GC）
////                channel.cancel()
//            }


            //sharedFlow 方式
//            lifecycleScope.launch {
//                supervisorScope {
//                    launch {
//                        delay(1000)
//                        dialogEventFlow.emit("1111")
//                    }
//                    launch {
//                        delay(3000)
//                        dialogEventFlow.emit("2222")
//                    }
//                    launch {
//                        delay(2000)
//                        dialogEventFlow.emit("333")
//                    }
//                }
//            }

        }

        thread {
            Thread.sleep(2000)
            runOnUiThread {
                dialogInterceptorChain.addInterceptor(DialogInterceptorImpl("111", false))
//                dialogInterceptorChain.proceed(this)
            }
        }

        thread {
            Thread.sleep(1000)
            runOnUiThread {
                dialogInterceptorChain.addInterceptor(DialogInterceptorImpl("222", false))
//                dialogInterceptorChain.proceed(this)
            }
        }

        thread {
            Thread.sleep(2700)
            runOnUiThread {
                dialogInterceptorChain.addInterceptor(DialogInterceptorImpl("333", false))
//                dialogInterceptorChain.proceed(this)
            }
        }

    }

}