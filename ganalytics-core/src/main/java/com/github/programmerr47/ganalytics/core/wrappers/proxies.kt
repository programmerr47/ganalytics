package com.github.programmerr47.ganalytics.core.wrappers

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Proxy

@Suppress("unchecked_cast")
internal inline fun <T> wrapObjMethods(clazz: Class<T>, crossinline handler: (Any, Method, Array<Any>?) -> Any): T {
    return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf<Class<*>>(clazz),
            ObjInvocationWrapper(clazz, InvocationHandler { obj, method, args -> handler.invoke(obj, method, args) })) as T
}

class ObjInvocationWrapper(
        private val clazz: Class<*>,
        private val origin: InvocationHandler) : InvocationHandler {
    private val fakeObj = Object()

    override fun invoke(ref: Any?, method: Method, args: Array<out Any>?): Any {
        if (method.isToString()) return "${clazz.simpleName}(Proxy)@${fakeObj.hashCode()}"
        else if (method.isHashCode()) return fakeObj.hashCode()
        else if (method.isEquals()) return ref === args?.get(0)
        else return origin.invoke(ref, method, args)
    }
}

private fun Method.isToString() =
        isPublic() && !isStatic() && !isFinal() &&
        name == "toString" &&
        parameterTypes.isEmpty() &&
        returnType == String::class.java

private fun Method.isHashCode() = isPublic() && !isStatic() && !isFinal() &&
        name == "hashCode" &&
        parameterTypes.isEmpty() &&
        returnType == Int::class.java

private fun Method.isEquals() = isPublic() && !isStatic() && !isFinal() &&
        name == "equals" &&
        parameterTypes.size == 1 && parameterTypes[0] == Object::class.java &&
        returnType == Boolean::class.java

private fun Method.isStatic() = Modifier.isStatic(modifiers)
private fun Method.isPublic() = Modifier.isPublic(modifiers)
private fun Method.isFinal() = Modifier.isFinal(modifiers)

