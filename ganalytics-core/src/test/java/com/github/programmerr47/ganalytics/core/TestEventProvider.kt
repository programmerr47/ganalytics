package com.github.programmerr47.ganalytics.core


class TestEventProvider : EventProvider {
    var lastEvent = Event("", "")
        private set

    override fun provide(event: Event) {
        lastEvent = event
    }
}