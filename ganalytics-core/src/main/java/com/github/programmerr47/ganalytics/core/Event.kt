package com.github.programmerr47.ganalytics.core

data class Event(
        val category: String,
        val action: String,
        val label: String = "",
        val value: Long = 0)