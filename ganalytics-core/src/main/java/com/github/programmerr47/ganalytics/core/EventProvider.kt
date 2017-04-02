package com.github.programmerr47.ganalytics.core

interface EventProvider {
    fun provide(event: Event)
}

inline fun EventProvider(crossinline provider: (Event) -> Unit) = object : EventProvider {
    override fun provide(event: Event) = provider(event)
}

fun compose(vararg provider: EventProvider) = object : EventProvider {
    override fun provide(event: Event) = provider.forEach { it.provide(event) }
}