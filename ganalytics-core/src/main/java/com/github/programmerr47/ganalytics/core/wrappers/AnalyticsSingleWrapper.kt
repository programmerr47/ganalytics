package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.*
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method

class AnalyticsSingleWrapper @JvmOverloads constructor(
        private val eventProvider: EventProvider,
        private val globalSettings: GanalyticsSettings = GanalyticsSettings(),
        private val defAnnotations: AnalyticsDefAnnotations = AnalyticsDefAnnotations()) : AnalyticsWrapper {
    private val actionArgsManager: ArgsManager by lazy { ActionArgsManager(globalSettings) }
    private val labelInterfaceCache: MutableMap<Pair<Event, NamingConvention>, AnalyticsWrapper> by lazy { HashMap<Pair<Event, NamingConvention>, AnalyticsWrapper>() }

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>) = wrapObjMethods(clazz) { _, method, args ->
        val convention = Convention::class.getFrom(clazz, defAnnotations)?.value ?: globalSettings.namingConvention
        val category = applyCategory(applyConvention(convention, clazz.analyticsName), clazz, defAnnotations)
        val action = applyAction(clazz, method, category, convention)

        val argsManager = resolveArgsMananger(method, convention, actionArgsManager)
        val (labelArg, valueArg) = argsManager.manage(method, args)
        val label = labelArg ?: ""
        val value = (valueArg ?: 0).toLong()

        val event = Event(category, action, label, value)

        resolveChaining(method, event, convention)
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

    private fun applyAction(clazz: Class<*>, method: Method, category: String, convention: NamingConvention): String {
        val defaultAction = applyAction(method, applyConvention(convention, method.name))
        val prefixAction = if (NoPrefix::class.getFrom(method, clazz, defAnnotations) != null) {
            defaultAction
        } else {
            applyPrefix(defaultAction, category, method, clazz, defAnnotations)
        }

        return if (NoPostfix::class.getFrom(method, clazz, defAnnotations) != null) {
            prefixAction
        } else {
            applyPostfix(prefixAction, method, clazz, defAnnotations)
        }
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

    private fun resolveChaining(method: Method, event: Event, convention: NamingConvention): Any {
        if (method.returnType != Unit::class.java && method.returnType != Void::class.java) {
            if (!event.label.isEmpty()) throw IllegalStateException("There can not be return type = ${method.returnType} when label is already set")

            return retrieveChainWrapper(event, convention).create(method.returnType)
        } else {
            return eventProvider.provide(event)
        }
    }

    private fun retrieveChainWrapper(event: Event, convention: NamingConvention): AnalyticsWrapper {
        val cacheKey = event to convention
        return labelInterfaceCache.getOrPut(cacheKey, { CacheWrapper(ChainWrapper(eventProvider, event, convention)) })
    }
}

inline fun AnalyticsSingleWrapper(crossinline provider: (Event) -> Unit,
                                  globalSettings: GanalyticsSettings = GanalyticsSettings()) =
        AnalyticsSingleWrapper(EventProvider(provider), globalSettings)
