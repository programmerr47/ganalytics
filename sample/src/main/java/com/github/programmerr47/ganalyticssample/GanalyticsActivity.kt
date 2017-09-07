package com.github.programmerr47.ganalyticssample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.programmerr47.ganalytics.core.*
import com.github.programmerr47.ganalytics.core.wrappers.AnalyticsGroupWrapper

import kotlinx.android.synthetic.main.activity_ganalytics.*

class GanalyticsActivity : AppCompatActivity() {

    private val placeholderStr: String by lazy { getString(R.string.no) }
    private val ganalytics: AnalyticsGroupWrapper by lazy { initGanalytics() }
    private val analytics: Analytics by lazy { ganalytics.create(Analytics::class) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganalytics)
        placeholderStr.let { Event(it, it, it) }.printOnScreen()

        b_car_price.setOnClickListener { analytics.car().price(Price(200, "dollars")) }
        b_car_sold.setOnClickListener { analytics.car().isSold() }
        b_car_type.setOnClickListener { analytics.car().type(CarType.CROSSOVER) }

        b_customer_buy.setOnClickListener { analytics.customer().buyCar(Car(CarType.SEDAN, Price(300, "euro"))) }
        b_customer_start.setOnClickListener { analytics.customer().startedSearching() }
        b_customer_end.setOnClickListener { analytics.customer().endedSearching() }

        b_seller_add.setOnClickListener { analytics.seller().addNewCarToSellList(Car(CarType.HATCHBACK, Price(256, "pounds"))) }
        b_seller_fair.setOnClickListener { analytics.seller().isFair() }
        b_seller_sell.setOnClickListener { analytics.seller().sellCar(Car(CarType.SUV, Price(500, "rubles"))) }
    }

    private fun initGanalytics() = Ganalytics({ it.printOnScreen() }) {
        prefixSplitter = "_"
        namingConvention = NamingConventions.LOWER_SNAKE_CASE
        labelTypeConverters += TypeConverterPair<Price> { it.userFriendlyStr() } +
                TypeConverterPair<Car> { it.run { "t: ${type.name.toLowerCase()}, p: ${price.userFriendlyStr()}" } }
    }

    private fun Price.userFriendlyStr() = "$amount $currency"

    private fun Event.printOnScreen() {
        tv_category.text = getString(R.string.category, category)
        tv_action.text = getString(R.string.action, action)
        tv_label.text = getString(R.string.label, if (label == "") placeholderStr else label)
    }
}
