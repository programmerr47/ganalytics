package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement
import kotlin.reflect.KClass

internal fun <T : Annotation> KClass<T>.getFrom(vararg elements: AnnotatedElement): T? {
    return elements.map { it.getAnnotation(java) }.firstOrNull { it != null }
}

internal fun applyConvention(convention: NamingConvention, name: String) = convention
        .withFirstFixingBadCodeStyle()
        .convert(name.decapitalize())

internal fun String.getOr(default: String) = takeNotEmpty() ?: default

internal fun String.takeNotEmpty() = takeUnless(String::isEmpty)
