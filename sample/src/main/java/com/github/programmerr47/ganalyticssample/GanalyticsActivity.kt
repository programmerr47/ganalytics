package com.github.programmerr47.ganalyticssample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.programmerr47.ganalytics.core.*

import kotlinx.android.synthetic.main.activity_ganalytics.*

class GanalyticsActivity : AppCompatActivity() {

    private val ganalytics: AnalyticsGroupWrapper by lazy { initGanalytics() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganalytics)
    }

    private fun initGanalytics() = Ganalytics({  }) {
        prefixSplitter = "_"
        namingConvention = NamingConventions.LOWER_SNAKE_CASE
        labelTypeConverters += TypeConverterPair<Price> { it.userFriendlyStr() } +
                TypeConverterPair<Car> { it.run { "t: ${type.name.toLowerCase()}, p: ${price.userFriendlyStr()}" } }
    }

    private fun Price.userFriendlyStr() = "$amount $currency"
}
