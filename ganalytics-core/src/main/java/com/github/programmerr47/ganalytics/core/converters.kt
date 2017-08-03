package com.github.programmerr47.ganalytics.core

typealias TypedConverterPair<T> = Pair<Class<out T>, TypedLabelConverter<out T>>
typealias AnyTypedConverterPair = TypedConverterPair<Any>

interface LabelConverter {
    fun convert(label: Any): String
}

object SimpleLabelConverter : LabelConverter {
    override fun convert(label: Any) = label.toString()
}

interface TypedLabelConverter<T> : LabelConverter {
    override fun convert(label: Any) = convertTyped(label as T)

    fun convertTyped(label: T): String
}

inline fun LabelConverter(crossinline converter: (Any) -> String) = object : LabelConverter {
    override fun convert(label: Any) = converter(label)
}

inline fun <T> TypeConverter(crossinline converter: (T) -> String) = object : TypedLabelConverter<T> {
    override fun convertTyped(label: T) = converter(label)
}

inline fun <reified T> TypeConverterPair(crossinline converter: (T) -> String) = T::class.java to TypeConverter(converter)

operator fun AnyTypedConverterPair.plus(other: AnyTypedConverterPair) = converters(this, other)