package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement

class AnalyticsSingleWrapper @JvmOverloads constructor(
        private val eventProvider: EventProvider,
        private val globalSettings: GanalyticsSettings = GanalyticsSettings(),
        private val defAnnotations: AnalyticsDefAnnotations = AnalyticsDefAnnotations()) : AnalyticsWrapper {
    private val actionArgsManager: ArgsManager by lazy { ActionArgsManager(globalSettings) }

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>) = wrapObjMethods(clazz) { _, method, args ->
        val convention = Convention::class.getFrom(clazz, defAnnotations)?.value ?: globalSettings.namingConvention
        val category = applyCategory(applyConvention(convention, clazz.analyticsName), clazz, defAnnotations)

        val defaultAction = applyAction(method, applyConvention(convention, method.name))
        val prefixAction = if (NoPrefix::class.getFrom(method, clazz, defAnnotations) != null) {
            defaultAction
        } else {
            applyPrefix(defaultAction, category, method, clazz, defAnnotations)
        }
        val finalAction = if (NoPostfix::class.getFrom(method, clazz, defAnnotations) != null) {
            prefixAction
        } else {
            applyPostfix(prefixAction, method, clazz, defAnnotations)
        }

        val argsManager = resolveArgsMananger(method, convention, actionArgsManager)
        val (labelArg, valueArg) = argsManager.manage(method, args)
        val label = labelArg ?: ""
        val value = (valueArg ?: 0).toLong()

        val event = Event(category, finalAction, label, value)
        eventProvider.provide(event)
    }

    private val Class<*>.analyticsName get() = if (globalSettings.cutOffAnalyticsClassPrefix)
        simpleName.decapitalize().removePrefix("analytics").capitalize()
    else
        simpleName

    private fun applyCategory(default: String, vararg elements: AnnotatedElement): String {
        return applyCategory(Category::class.getFrom(*elements), default)
    }

    private fun applyCategory(category: Category?, default: String): String {
        return category?.name?.takeNotEmpty() ?: default
    }

    private fun applyAction(element: AnnotatedElement, default: String): String {
        return element.getAnnotation(LabelFun::class.java)?.action ?:
                element.getAnnotation(Action::class.java)?.name?.takeNotEmpty() ?:
                default
    }

    private fun applyPostfix(input: String, vararg elements: AnnotatedElement): String {
        return applyPostfix(input, HasPostfix::class.getFrom(*elements))
    }

    private fun applyPostfix(input: String, hasPostfix: HasPostfix?): String {
        return input + (hasPostfix?.run { globalSplitter + name } ?: "")
    }

    private val HasPostfix.globalSplitter get() = splitter.getOr(globalSettings.postfixSplitter)

    private fun applyPrefix(input: String, default: String, vararg elements: AnnotatedElement): String {
        return applyPrefix(input, default, HasPrefix::class.getFrom(*elements))
    }

    private fun applyPrefix(input: String, default: String, hasPrefix: HasPrefix?): String {
        return if (hasPrefix == null) input else applyPrefix(input, default, hasPrefix.name, hasPrefix.globalSplitter)
    }

    private val HasPrefix.globalSplitter get() = splitter.getOr(globalSettings.prefixSplitter)

    private fun applyPrefix(input: String, default: String, prefix: String, splitter: String): String {
        return (prefix.getOr(default)) + splitter + input
    }

    private fun resolveArgsMananger(element: AnnotatedElement, convention: NamingConvention, default: ArgsManager): ArgsManager {
        return if (element.getAnnotation(LabelFun::class.java) != null) {
            if (element.getAnnotation(Action::class.java) != null) {
                throw IllegalStateException("@Action and @LabelFun incompatible. Please, use one of them")
            }

            LabelArgsManager(convention)
        } else default
    }
}

inline fun AnalyticsSingleWrapper(crossinline provider: (Event) -> Unit,
                                  globalSettings: GanalyticsSettings = GanalyticsSettings()) =
        AnalyticsSingleWrapper(EventProvider(provider), globalSettings)
