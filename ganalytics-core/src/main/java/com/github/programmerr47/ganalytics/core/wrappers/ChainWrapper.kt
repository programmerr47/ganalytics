package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.*

class ChainWrapper(
        private val eventProvider: EventProvider,
        private val event: Event,
        private val convention: NamingConvention
) : AnalyticsWrapper {
    private val argsManager: ArgsManager by lazy { LabelArgsManager(convention) }

    override fun <T : Any> create(clazz: Class<T>) = wrapObjMethods(clazz) { _, method, args ->
        var (label, value) = argsManager.manage(method, args)
        label = label ?: ""
        value = (value ?: 0).toLong()

        val newEvent = event.copy(label = label, value = value)
        eventProvider.provide(newEvent)
    }
}