package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Method
import kotlin.reflect.KClass

interface ArgsManager {
    fun manage(method: Method, args: Array<Any>?): Pair<String?, Number?>
}

class LabelArgsManager(
        private val convention: NamingConvention) : ArgsManager {
    override fun manage(method: Method, args: Array<Any>?) = when (args?.size) {
        in arrayOf(0, null) -> buildPair(method, null)
        1 -> buildPair(method, manageArgAsValue(method, args))
        else -> throw IllegalArgumentException("Method ${method.name} are label, so it can have up to 1 parameter, which is value")
    }

    private fun buildPair(method: Method, value: Number?) = Pair(buildLabel(method), value)

    private fun buildLabel(method: Method): String {
        return method.getAnnotation(LabelFun::class.java)?.label?.takeNotEmpty() ?:
                applyConvention(convention, method.name)
    }

    private fun manageArgAsValue(method: Method, args: Array<Any>): Number? {
        return manageValueArg(method, args[0], method.parameterAnnotations[0].label())
    }

    private fun manageValueArg(method: Method, vArg: Any, vArgA: Label?) = if (vArgA != null) {
        throw IllegalArgumentException("Method ${method.name} can not have @Label annotation on parameters, since it is already a label")
    } else {
        vArg as? Number ?:
                throw IllegalArgumentException("Method ${method.name} can have only 1 parameter which must be a Number")
    }
}

class ActionArgsManager(
        private val globalSettings: GanalyticsSettings) : ArgsManager {

    override fun manage(method: Method, args: Array<Any>?) = when (args?.size) {
        in arrayOf(0, null) -> Pair(null, null)
        1 -> Pair(convertLabelArg(args[0], method.parameterAnnotations[0]), null)
        2 -> manageTwoArgs(args, method.parameterAnnotations)
        else -> throw IllegalArgumentException("Method ${method.name} have ${method.parameterCount} parameter(s). You can have up to 2 parameters in ordinary methods.")
    }

    private fun manageTwoArgs(args: Array<Any>, annotations: Array<Array<Annotation>>): Pair<String, Number> {
        return manageTwoArgs(args[0], annotations[0].label(), args[1], annotations[1].label())
    }

    private fun manageTwoArgs(arg1: Any, argA1: Label?, arg2: Any, argA2: Label?): Pair<String, Number> {
        return manageArgAsValue(arg2, argA2, arg1, argA1) {
            manageArgAsValue(arg1, argA1, arg2, argA2) {
                throw IllegalArgumentException("For methods with 2 parameters one of them have to be Number without Label annotation")
            }
        }
    }

    private inline fun manageArgAsValue(vArg: Any, vArgA: Label?, lArg: Any, lArgA: Label?, defaultAction: () -> Pair<String, Number>): Pair<String, Number> {
        return if (vArg is Number && vArgA == null) {
            Pair(convertLabelArg(lArg, lArgA), vArg)
        } else {
            defaultAction()
        }
    }

    private fun convertLabelArg(label: Any, annotations: Array<Annotation>): String {
        return convertLabelArg(label, annotations.firstOrNull(Label::class))
    }

    private fun convertLabelArg(label: Any, annotation: Label?): String {
        return chooseConverter(label, annotation).convert(label)
    }

    private fun chooseConverter(label: Any, annotation: Label?): LabelConverter {
        return annotation?.converter?.init() ?: lookupGlobalConverter(label) ?: SimpleLabelConverter
    }

    private fun lookupGlobalConverter(label: Any): LabelConverter? {
        label.converterClasses().forEach {
            val converter = globalSettings.labelTypeConverters.lookup(it)
            if (converter != null) return converter
        }
        return null
    }

    private fun Any.converterClasses() = if (globalSettings.useTypeConvertersForSubType)
        javaClass.classHierarchy()
    else
        arrayListOf(javaClass)

    private fun Class<in Any>.classHierarchy() = ArrayList<Class<Any>>().also {
        var clazz: Class<in Any>? = this
        do {
            it.add(clazz!!)
            clazz = clazz.superclass
        } while (clazz != null)
    }

    private fun KClass<out LabelConverter>.init() = objectInstance ?: java.newInstance()
}

private fun Array<Annotation>.label() = firstOrNull(Label::class)

private fun <R : Any> Array<*>.firstOrNull(klass: KClass<R>): R? {
    return filterIsInstance(klass.java).firstOrNull()
}
