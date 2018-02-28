package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Event
import com.tgirard12.cqrssimple.EventBus
import com.tgirard12.cqrssimple.EventHandlerBase

class AEvent : Event
class BEvent : Event
class CEvent : Event
class DEvent : Event

object EventCount {
    fun reset() {
        aCount = "a"
        bCount = "b"
        cCount = "c"
        dCount = "d"
    }

    lateinit var aCount: String
    lateinit var bCount: String
    lateinit var cCount: String
    lateinit var dCount: String
}

class AEventHandler : EventHandlerBase<AEvent>() {
    override fun handle(event: AEvent): Unit {
        EventCount.aCount += "1"
    }
}

class BEventHandler : EventHandlerBase<BEvent>({ EventCount.bCount += "2" })

val cEventHandler = object : EventHandlerBase<CEvent>({ EventCount.cCount += "3" }) {}
val cEventHandler2 = object : EventHandlerBase<CEvent>({ EventCount.cCount += "4" }) {}


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class EventBusStub : EventBus, MockStub {
    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()
    override val mockTime: MutableList<Long> = mutableListOf()

    var _dispatch = { }
    override fun dispatch(event: Event): Unit {
        mockFun.add("dispatch")
        mockData.add(listOf(event))
        mockTime.add(System.nanoTime())
        return _dispatch()
    }
}
