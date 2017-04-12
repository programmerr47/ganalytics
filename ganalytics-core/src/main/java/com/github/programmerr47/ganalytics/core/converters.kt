package com.github.programmerr47.ganalytics.core

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