package com.github.programmerr47.ganalytics.core

open class DummyClass(val id: Int, val name: String)

data class DummyDataClass(val id: Int, val name: String)

class DummyReversedClass(id: Int, name: String) : DummyClass(id, name.reversed())

enum class DummyEnum { ONE, TWO, THREE }
