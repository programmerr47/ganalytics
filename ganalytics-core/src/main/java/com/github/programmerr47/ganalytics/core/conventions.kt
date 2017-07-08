package com.github.programmerr47.ganalytics.core

interface NamingConvention {
    fun convert(name: String): String
}

enum class NamingConventions(val converter: (String) -> String) : NamingConvention {
    UPPER_SNAKE_CASE({ LOWER_SNAKE_CASE.convert(it).capitalize() }),
    LOWER_SNAKE_CASE({ it.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").toLowerCase() }),
    UPPER_CAMEL_CASE(String::capitalize),
    LOWER_CAMEL_CASE(String::decapitalize),
    LOWER_CASE(String::toLowerCase);

    override fun convert(name: String) = converter(name)
}

internal fun fixingBadCodeStyleConvention() = object : NamingConvention {
    override fun convert(name: String) = name
            .split('_', ' ')
            .joinToString(
                    separator = "",
                    transform = String::capitalize)
}