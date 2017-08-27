package com.github.programmerr47.ganalytics.core

import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(CLASS, FUNCTION, PROPERTY)
annotation class HasPrefix(val name: String = "", val splitter: String = "")

@Target(CLASS, FUNCTION, PROPERTY)
annotation class HasPostfix(val name: String, val splitter: String = "")

@Target(CLASS, FUNCTION, PROPERTY)
annotation class NoPrefix

@Target(CLASS, FUNCTION, PROPERTY)
annotation class NoPostfix

@Target(CLASS, FUNCTION, PROPERTY)
annotation class Category(val name: String = "")

@Target(FUNCTION)
annotation class Action(val name: String = "")

@Target(VALUE_PARAMETER)
annotation class Label(val converter: KClass<out LabelConverter> = SimpleLabelConverter::class)

@Target(FUNCTION)
annotation class LabelFun(val action: String)

@Target(CLASS, FUNCTION, PROPERTY)
annotation class Convention(val value: NamingConventions = NamingConventions.LOWER_CASE)