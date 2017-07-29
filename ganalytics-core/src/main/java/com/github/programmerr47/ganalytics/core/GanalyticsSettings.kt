package com.github.programmerr47.ganalytics.core

class ConcatList<T>(private val origin: MutableList<T>) {

    operator fun plus(item: T) = apply { origin.add(item) }
    operator fun plusAssign(item: ConcatList<T>) {
        origin.addAll(item.origin)
    }
}

fun <T> concatListOf(vararg elements: T): ConcatList<T> = ConcatList(mutableListOf(*elements))

class GanalyticsSettings {
    var prefixSplitter: String = ""
    var namingConvention: NamingConvention = NamingConventions.LOWER_CASE
    var cutOffAnalyticsClassPrefix: Boolean = true
//    var labelTypeConverters: ConcatList<LabelConverter> = concatListOf()
}

inline fun GanalyticsSettings(init: GanalyticsSettings.() -> Unit) = GanalyticsSettings().apply { init() }

fun GanalyticsSettings.createGroup(eventProvider: EventProvider) = AnalyticsGroupWrapper(eventProvider)
inline fun GanalyticsSettings.createGroup(crossinline eventProvider: (Event) -> Unit) = AnalyticsGroupWrapper(eventProvider)

inline fun Ganalytics(eventProvider: EventProvider, init: GanalyticsSettings.() -> Unit) = GanalyticsSettings(init).createGroup(eventProvider)
inline fun Ganalytics(crossinline eventProvider: (Event) -> Unit, init: GanalyticsSettings.() -> Unit) = GanalyticsSettings(init).createGroup(eventProvider)