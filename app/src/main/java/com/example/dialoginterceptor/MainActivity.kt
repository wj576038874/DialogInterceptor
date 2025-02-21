package com.example.dialoginterceptor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val dialogInterceptorChain = DialogInterceptorChain(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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