package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class AnalyticsSingleWrapper(private val eventProvider: EventProvider) : AnalyticsWrapper {
    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            val category = applyCategory(clazz, clazz.simpleName.toLowerCase().removePrefix("analytics"))

            val defaultAction = applyAction(method, method.name)
            val action = if (getAnnotation(NoPrefix::class, method, clazz) != null) {
                defaultAction
            } else {
                applyPrefix(defaultAction, category, method, clazz)
            }

            val (labelArg, valueArg) = manageLabelValueArgs(method, args)
            val label = (labelArg ?: "").toString()
            val value = (valueArg ?: 0).toLong()

            val event = Event(category, action, label, value)
            eventProvider.provide(event)
        } as T
    }

    private fun applyCategory(element: AnnotatedElement, default: String) =
            applyCategory(element.getAnnotation(Category::class.java), default)

    private fun applyCategory(category: Category?, default: String): String {
        return category?.name?.takeNotEmpty() ?: default
    }

    private fun applyAction(element: AnnotatedElement, default: String): String {
        return applyAction(element.getAnnotation(Action::class.java), default)
    }

    private fun applyAction(action: Action?, default: String): String {
        return action?.name?.takeNotEmpty() ?: default
    }

    private fun applyPrefix(input: String, default: String, vararg elements: AnnotatedElement): String {
        return applyPrefix(input, default, getAnnotation(HasPrefix::class, *elements))
    }

    private fun applyPrefix(input: String, default: String, hasPrefix: HasPrefix?): String {
        return if (hasPrefix == null) input else applyPrefix(input, default, hasPrefix.name, hasPrefix.splitter)
    }

    private fun applyPrefix(input: String, default: String, prefix: String, splitter: String): String {
        return (prefix.getOr(default)) + splitter + input
    }

    private fun String.getOr(default: String) = takeNotEmpty() ?: default

    private fun String.takeNotEmpty() = takeUnless(String::isEmpty)

    private fun <T : Annotation> getAnnotation(clazz: KClass<T>, vararg elements: AnnotatedElement): T? {
        return elements.map { it.getAnnotation(clazz.java) }.firstOrNull { it != null }
    }

    private fun manageLabelValueArgs(method: Method, args: Array<Any>?) = when (args?.size) {
        in arrayOf(0, null) -> Pair(null, null)
        1 -> Pair(args[0], null)
        2 -> manageTwoArgs(args, method.parameterAnnotations)
        else -> throw IllegalArgumentException("Method ${method.name} have ${method.parameterCount} parameter(s). You can have up to 2 parameters in methods.")
    }

    private fun manageTwoArgs(args: Array<Any>, annotations: Array<Array<Annotation>>): Pair<Any, Number> {
        val firstArg = args[0]
        val secondArg = args[1]

        if (secondArg is Number) {
            if (annotations[1].firstOrNull { it is Label } != null) {
                if (annotations[0].firstOrNull { it is Label } != null) {
                    throw IllegalArgumentException("Methods with two parameters can have no more than 1 Label annotation")
                } else {
                    if (firstArg is Number) {
                        return Pair(secondArg, firstArg)
                    } else {
                        throw IllegalArgumentException("For methods with 2 parameters one of them have to be Number without Label annotation")
                    }
                }
            } else {
                return Pair(firstArg, secondArg)
            }
        } else if (firstArg is Number) {
            if (annotations[0].firstOrNull { it is Label } != null) {
                throw IllegalArgumentException("For methods with 2 parameters one of them have to be Number without Label annotation")
            } else {
                return Pair(secondArg, firstArg)
            }
        } else {
            throw IllegalArgumentException("For methods with 2 parameters one of them have to be Number without Label annotation")
        }
    }
}
