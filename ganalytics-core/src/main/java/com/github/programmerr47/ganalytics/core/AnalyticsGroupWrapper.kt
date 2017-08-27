package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

internal typealias AnnotationClasses = List<KClass<out Annotation>>

class AnalyticsGroupWrapper(
        private val eventProvider: EventProvider,
        private val globalSettings: GanalyticsSettings = GanalyticsSettings()) : AnalyticsWrapper {
    internal val nameCache: MutableMap<String, AnalyticsWrapper> by lazy { HashMap<String, AnalyticsWrapper>() }
    internal val smartCache: MutableMap<String, AnalyticsWrapper> by lazy { HashMap<String, AnalyticsWrapper>() }

    private val notSmartAnnotations = listOf(HasPrefix::class, NoPrefix::class, Convention::class)
    private val requiredAnnotations = notSmartAnnotations + Category::class

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { _, method, _ ->
            getWrapper(method, clazz, requiredAnnotations, notSmartAnnotations).create(method.returnType)
        } as T
    }

    private fun getWrapper(method: Method, clazz: Class<*>, required: AnnotationClasses, notSmart: AnnotationClasses): AnalyticsWrapper {
        return getWrapper(method, clazz,
                notSmart.getAnnotations(method, clazz),
                AnalyticsDefAnnotations(required.getAnnotations(method, clazz).toTypedArray()))
    }

    private fun getWrapper(method: Method, clazz: Class<*>, notSmartAnnotations: List<Annotation>, defAnnotations: AnalyticsDefAnnotations): AnalyticsWrapper {
        return if (notSmartAnnotations.isEmpty()) {
            val category = Category::class.getFrom(method, clazz)
            smartCache.getOrPut({ category?.name ?: "" }, defAnnotations)
        } else {
            nameCache.getOrPut({ generateKey(clazz, method) }, defAnnotations)
        }
    }

    private fun <T : KClass<out Annotation>> Collection<T>.getAnnotations(method: Method, clazz: Class<*>): List<Annotation> {
        return mapNotNull { it.getFrom(method, clazz) }
    }

    private inline fun <K> MutableMap<K, AnalyticsWrapper>.getOrPut(keygen: () -> K, defAnnotations: AnalyticsDefAnnotations): AnalyticsWrapper {
        return synchronized(this) {
            getOrPut(keygen()) { CacheWrapper(AnalyticsSingleWrapper(eventProvider, globalSettings, defAnnotations)) }
        }
    }

    private fun generateKey(clazz: Class<*>, method: Method) = "${clazz.simpleName}.${method.name}"
}

inline fun AnalyticsGroupWrapper(crossinline provider: (Event) -> Unit,
                                 globalSettings: GanalyticsSettings = GanalyticsSettings()) =
        AnalyticsGroupWrapper(EventProvider(provider), globalSettings)
