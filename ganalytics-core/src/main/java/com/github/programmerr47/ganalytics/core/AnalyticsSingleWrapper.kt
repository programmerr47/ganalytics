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
            val value = ((valueArg ?: 0) as Number).toLong()

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
        1 -> findOrNull(args, String::class.java).to(null)
        2 -> try { manageTwoArgs(args) } catch (e: NoSuchElementException) { throw IllegalArgumentException("For methods with 2 parameters one of them have to be Number", e) }
        else -> throw IllegalArgumentException("Method ${method.name} have ${method.parameterCount} parameter(s). You can have up to 2 parameters in methods.")
    }

    private fun manageTwoArgs(args: Array<Any>): Pair<Any?, Any?> {
        val valueArg = findStrongNotNull(args, Number::class.java)
        val labelArg = findOrNull(args, String::class.java, arrayOf(valueArg))
        return labelArg.to(valueArg)
    }

    private fun findStrongNotNull(args: Array<Any>, clazz: Class<*>, reserved: Array<Any?> = arrayOf()): Any {
        return findStrong(args, { first(it) }, clazz, reserved)
    }

    private fun findOrNull(args: Array<Any>, clazz: Class<*>, reserved: Array<Any?> = arrayOf()): Any? {
        return find(args, { firstOrNull(it) }, clazz, reserved)
    }

    private inline fun <R> find(args: Array<Any>, action: Array<out Any>.((Any) -> Boolean) -> R, clazz: Class<*>, reserved: Array<Any?> = arrayOf()): R {
        return find(args, action, { clazz.isInstance(it) && !reserved.contains(it) }, { !reserved.contains(it) })
    }

    private inline fun <R> findStrong(args: Array<Any>, action: Array<out Any>.((Any) -> Boolean) -> R, clazz: Class<*>, reserved: Array<Any?> = arrayOf()): R {
        return find(args, action, { clazz.isInstance(it) && !reserved.contains(it) })
    }

    private inline fun <R> find(args: Array<Any>, action: Array<out Any>.((Any) -> Boolean) -> R, vararg predicate: (Any) -> Boolean): R {
        return args.action(findFirstPredicate(args, *predicate) ?: {false})
    }

    private fun findFirstPredicate(args: Array<Any>, vararg predicate: (Any) -> Boolean): ((Any) -> Boolean)? {
        return predicate.firstOrNull { args.firstOrNull(it) != null }
    }
}
