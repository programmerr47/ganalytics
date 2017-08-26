package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class AnalyticsSingleWrapper(
        private val eventProvider: EventProvider,
        private val globalSettings: GanalyticsSettings = GanalyticsSettings(),
        private val defAnnotations: AnalyticsDefAnnotations = AnalyticsDefAnnotations()) : AnalyticsWrapper {
    private val actionArgsManager: ArgsManager by lazy { ActionArgsManager(globalSettings) }

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { _, method, args ->
            val convention = getAnnotation(Convention::class, clazz, defAnnotations)?.value ?: globalSettings.namingConvention
            val category = applyCategory(applyConvention(convention, clazz.analyticsName), clazz, defAnnotations)

            val defaultAction = applyAction(method, applyConvention(convention, method.name))
            val action = if (getAnnotation(NoPrefix::class, method, clazz, defAnnotations) != null) {
                defaultAction
            } else {
                applyPrefix(defaultAction, category, method, clazz, defAnnotations)
            }

            val (labelArg, valueArg) = actionArgsManager.manage(method, args)
            val label = labelArg ?: ""
            val value = (valueArg ?: 0).toLong()

            val event = Event(category, action, label, value)
            eventProvider.provide(event)
        } as T
    }

    private val Class<*>.analyticsName get() = if (globalSettings.cutOffAnalyticsClassPrefix)
        simpleName.decapitalize().removePrefix("analytics").capitalize()
    else
        simpleName

    private fun applyConvention(convention: NamingConvention, name: String) = convention
            .withFirstFixingBadCodeStyle()
            .convert(name.decapitalize())

    private fun applyCategory(default: String, vararg elements: AnnotatedElement) =
            applyCategory(getAnnotation(Category::class, *elements), default)

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
        return if (hasPrefix == null) input else applyPrefix(input, default, hasPrefix.name, hasPrefix.globalSplitter)
    }

    private val HasPrefix.globalSplitter get() = if (splitter == "") globalSettings.prefixSplitter else splitter

    private fun applyPrefix(input: String, default: String, prefix: String, splitter: String): String {
        return (prefix.getOr(default)) + splitter + input
    }

    private fun String.getOr(default: String) = takeNotEmpty() ?: default

    private fun String.takeNotEmpty() = takeUnless(String::isEmpty)

    private fun <T : Annotation> getAnnotation(clazz: KClass<T>, vararg elements: AnnotatedElement): T? {
        return elements.map { it.getAnnotation(clazz.java) }.firstOrNull { it != null }
    }
}

inline fun AnalyticsSingleWrapper(crossinline provider: (Event) -> Unit,
                                 globalSettings: GanalyticsSettings = GanalyticsSettings()) =
        AnalyticsSingleWrapper(EventProvider(provider), globalSettings)
