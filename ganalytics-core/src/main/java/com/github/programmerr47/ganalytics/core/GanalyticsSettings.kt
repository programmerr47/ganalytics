package com.github.programmerr47.ganalytics.core

class ConcatList(private val origin: HashMap<Class<out Any>, TypedLabelConverter<out Any>>) {

    operator fun plus(item: Pair<Class<out Any>, TypedLabelConverter<out Any>>) = apply {
        origin.put(item.first, item.second)
    }

    operator fun plusAssign(item: ConcatList) {
        origin.putAll(item.origin)
    }

    fun lookup(clazz: Class<Any>) = origin[clazz]
}

fun <T : Any> converters(vararg elements: TypedConverterPair<T>) = ConcatList(hashMapOf(*elements))

class GanalyticsSettings {
    var prefixSplitter: String = ""
    var postfixSplitter: String = ""
    var namingConvention: NamingConvention = NamingConventions.LOWER_CASE
    var cutOffAnalyticsClassPrefix: Boolean = true
    var labelTypeConverters: ConcatList = converters<Any>()
    var useTypeConvertersForSubType: Boolean = true

    fun <T : Any> addTypeConverter(type: Class<T>, converter: (T) -> String) = apply {
        labelTypeConverters += type to TypeConverter(converter)
    }
}

inline fun GanalyticsSettings(init: GanalyticsSettings.() -> Unit) = GanalyticsSettings().apply { init() }

fun GanalyticsSettings.createGroup(eventProvider: EventProvider) = AnalyticsGroupWrapper(eventProvider, this)
inline fun GanalyticsSettings.createGroup(crossinline eventProvider: (Event) -> Unit) = AnalyticsGroupWrapper(eventProvider, this)

fun GanalyticsSettings.createSingle(eventProvider: EventProvider) = AnalyticsSingleWrapper(eventProvider, this)
inline fun GanalyticsSettings.createSingle(crossinline eventProvider: (Event) -> Unit) = AnalyticsSingleWrapper(eventProvider, this)

inline fun Ganalytics(eventProvider: EventProvider, init: GanalyticsSettings.() -> Unit) = GanalyticsSettings(init).createGroup(eventProvider)
inline fun Ganalytics(crossinline eventProvider: (Event) -> Unit, init: GanalyticsSettings.() -> Unit) = GanalyticsSettings(init).createGroup(eventProvider)