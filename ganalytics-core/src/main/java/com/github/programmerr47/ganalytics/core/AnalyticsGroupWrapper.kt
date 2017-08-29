package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

internal typealias Annotations = List<Annotation>

class AnalyticsGroupWrapper(
        private val eventProvider: EventProvider,
        private val globalSettings: GanalyticsSettings = GanalyticsSettings()) : AnalyticsWrapper {
    internal val nameCache: MutableMap<String, AnalyticsWrapper> by lazy { HashMap<String, AnalyticsWrapper>() }
    internal val smartCache: MutableMap<String, AnalyticsWrapper> by lazy { HashMap<String, AnalyticsWrapper>() }

    private val notSmartAnnotations = listOf(
            HasPrefix::class, NoPrefix::class,
            HasPostfix::class, NoPostfix::class,
            Convention::class)

    private val requiredAnnotations = notSmartAnnotations + Category::class

    @Suppress("unchecked_cast")
    override fun <T : Any> create(kClass: KClass<T>): T {
        return kClass.run { createProxy(java, memberProperties) } as T
    }

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>) = createProxy(clazz) as T

    private fun createProxy(clazz: Class<*>, props: Iterable<KProperty1<*, *>> = listOf()): Any {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz)) { _, method, _ ->
            val prop = props.find(method)
            val propAnnotations = prop?.annotations ?: listOf()
            val transform: (KClass<out Annotation>) -> Annotation? = {
                val annClass = it
                propAnnotations.find { annClass.isInstance(it) } ?: it.getFrom(method, clazz)
            }
            val methodAnnotations = requiredAnnotations.mapNotNull(transform)
            val notSmartMethodAnnotations = notSmartAnnotations.mapNotNull(transform)
            val defAnnotations = AnalyticsDefAnnotations(methodAnnotations.toTypedArray())
            getWrapper(prop, method, clazz, notSmartMethodAnnotations, defAnnotations).create(method.returnType)
        }
    }

    private fun Iterable<KProperty1<*, *>>.find(method: Method): KProperty1<*, *>? {
        if (method.parameterCount != 0) return null
        return find { convert(it.getter.name) == method.name }
    }

    private fun convert(getter: String) = getter
            .substringAfter("<")
            .substringBefore(">")
            .split('-')
            .map(String::capitalize)
            .joinToString(separator = "")
            .decapitalize()

    private fun getWrapper(prop: KProperty1<*,*>?, method: Method, clazz: Class<*>, notSmartAnnotations: Annotations, defAnnotations: AnalyticsDefAnnotations): AnalyticsWrapper {
        return if (notSmartAnnotations.isEmpty()) {
            val category = Category::class.run { prop?.annotations?.find { isInstance(it) } as Category? ?: getFrom(method, clazz) }
            smartCache.getOrPut({ category?.name ?: "" }, defAnnotations)
        } else {
            nameCache.getOrPut({ generateKey(clazz, method) }, defAnnotations)
        }
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
