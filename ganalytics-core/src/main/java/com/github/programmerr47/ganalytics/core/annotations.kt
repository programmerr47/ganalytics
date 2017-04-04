package com.github.programmerr47.ganalytics.core

import kotlin.annotation.AnnotationTarget.*

@Target(CLASS, FUNCTION)
annotation class HasPrefix(val name: String = "", val splitter: String = "")

@Target(CLASS, FUNCTION)
annotation class NoPrefix

@Target(CLASS)
annotation class Category(val name: String = "")