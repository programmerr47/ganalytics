package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement
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
            val event = Event(category, action)
            eventProvider.provide(event)
        } as T
    }

    private fun applyCategory(element: AnnotatedElement, default: String) =
            applyCategory(element.getAnnotation(Category::class.java), default)

    private fun applyCategory(category: Category?, default: String): String {
        return if (category == null) default else apply(category.name, default)
    }

    private fun applyAction(element: AnnotatedElement, default: String): String {
        return applyAction(element.getAnnotation(Action::class.java), default)
    }

    private fun applyAction(action: Action?, default: String): String {
        return if (action == null) default else apply(action.name, default)
    }

    private fun applyPrefix(input: String, default: String, vararg elements: AnnotatedElement): String {
        return applyPrefix(input, default, getAnnotation(HasPrefix::class, *elements))
    }

    private fun applyPrefix(input: String, default: String, hasPrefix: HasPrefix?): String {
        return if (hasPrefix == null) input else applyPrefix(input, default, hasPrefix.name, hasPrefix.splitter)
    }

    private fun applyPrefix(input: String, default: String, prefix: String, splitter: String): String {
        return (apply(prefix, default)) + splitter + input
    }

    private fun apply(target: String, default: String): String {
        return if (target.isEmpty()) default else target
    }

    private fun <T : Annotation> getAnnotation(clazz: KClass<T>, vararg elements: AnnotatedElement): T? {
        return elements.map { it.getAnnotation(clazz.java) }.firstOrNull { it != null }
    }
}
