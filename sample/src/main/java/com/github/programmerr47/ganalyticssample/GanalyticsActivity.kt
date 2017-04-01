package com.github.programmerr47.ganalyticssample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.programmerr47.ganalytics.core.MyClass

import kotlinx.android.synthetic.main.activity_ganalytics.*

class GanalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganalytics)
        val libClass = MyClass(23)
        tv_greetings.text = "Hello Ganalytics with state $libClass"
    }
}
