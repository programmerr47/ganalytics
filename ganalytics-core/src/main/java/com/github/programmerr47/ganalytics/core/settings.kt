package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement

class AnalyticsDefAnnotations(private val annotations: Array<Annotation> = emptyArray()) : AnnotatedElement {
    override fun getAnnotations() = annotations
    override fun getDeclaredAnnotations() = annotations

    override fun <T : Annotation?> getAnnotation(p0: Class<T>?) = annotations.firstOrNull { p0?.isInstance(it) ?: false } as T?
}