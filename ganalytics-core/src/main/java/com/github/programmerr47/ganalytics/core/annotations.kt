package com.github.programmerr47.ganalytics.core

import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(CLASS, FUNCTION)
annotation class HasPrefix(val name: String = "", val splitter: String = "")

@Target(CLASS, FUNCTION)
annotation class NoPrefix

@Target(CLASS, FUNCTION)
annotation class Category(val name: String = "")

@Target(FUNCTION)
annotation class Action(val name: String = "")

@Target(VALUE_PARAMETER)
annotation class Label(val converter: KClass<out LabelConverter> = SimpleLabelConverter::class)

@Target(FUNCTION)
annotation class LabelFun(val action: String)

@Target(CLASS, FUNCTION)
annotation class Convention(val value: NamingConventions = NamingConventions.LOWER_CASE)