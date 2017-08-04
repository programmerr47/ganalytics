package com.github.programmerr47.ganalytics.core

open class DummyClass(val id: Int, val name: String) {
    override fun toString() = "DummyClass(id=$id, name=$name)"
}

data class DummyDataClass(val id: Int, val name: String)

class DummyReversedClass(id: Int, name: String) : DummyClass(id, name.reversed()) {
    override fun toString() = "DummyReversedClass(id=$id, name=$name)"
}

enum class DummyEnum { ONE, TWO, THREE }
