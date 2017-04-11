package com.github.programmerr47.ganalytics.core

interface LabelConverter<in T> {
    fun convert(label: T): String
}

object SimpleLabelConverter : LabelConverter<Any> {
    override fun convert(label: Any) = label.toString()
}