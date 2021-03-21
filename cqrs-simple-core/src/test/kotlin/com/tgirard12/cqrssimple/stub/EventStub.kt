package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Event
import com.tgirard12.cqrssimple.EventHandler

class AEvent : Event
class BEvent : Event
class CEvent : Event
class DEvent : Event

object EventCount {
    lateinit var sequence: String

    fun add(id: String) {
        sequence += id
    }

    fun reset() {
        sequence = ""
    }
}

class AEventHandler : EventHandler<AEvent> {
    override fun handle(event: AEvent) {
        EventCount.add("a1")
    }
}

class BEventHandler : EventHandler<BEvent> {
    override fun handle(event: BEvent) {
        EventCount.add("b2")
    }
}

val cEventHandler = EventHandler<CEvent> { EventCount.add("c3") }
val cEventHandler2 = EventHandler<CEvent> { EventCount.add("C4") }
