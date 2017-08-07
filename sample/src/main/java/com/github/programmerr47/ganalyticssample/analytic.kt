package com.github.programmerr47.ganalyticssample

import com.github.programmerr47.ganalytics.core.*

interface Analytics {
    fun car(): AnalyticsCar
    fun seller(): AnalyticsSeller
    @HasPrefix fun customer(): AnalyticsCustomer
}

interface AnalyticsCar {
    fun isSold()
    fun type(carType: CarType)
    fun price(price: Price)
}

@HasPrefix
interface AnalyticsSeller {
    @NoPrefix fun isFair()
    fun sellCar(car: Car)
    @Action("add_car") fun addNewCarToSellList(car: Car)
}

interface AnalyticsCustomer {
    fun buyCar(@Label(CustomerCarConverter::class) car: Car)
    fun startedSearching()
    fun endedSearching()
}


//---------------------------------


class Car(val type: CarType, val price: Price)

enum class CarType {
    SEDAN, HATCHBACK, SUV, CROSSOVER
}

class Price(val amount: Int, val currency: String)

object CustomerCarConverter : TypedLabelConverter<Car> {
    override fun convertTyped(label: Car) = label.run { "$type with price = ${price.amount}" }
}
