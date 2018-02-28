package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Event
import com.tgirard12.cqrssimple.EventBus
import com.tgirard12.cqrssimple.EventHandlerBase

class AEvent : Event
class BEvent : Event
class CEvent : Event
class DEvent : Event

object EventCount {
    var aCount = 0
    var bCount = 100
    var cCount = 1_000
    var dCount = 10_000
}

class AEventHandler : EventHandlerBase<AEvent>() {
    override fun handle(event: AEvent): Unit {
        EventCount.aCount++
    }
}

class BEventHandler : EventHandlerBase<BEvent>({ EventCount.bCount++ })

val cEventHandler = object : EventHandlerBase<CEvent>({ EventCount.cCount += 1 }) {}
val cEventHandler2 = object : EventHandlerBase<CEvent>({ EventCount.cCount += 10 }) {}


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class EventBusStub : EventBus, MockStub {
    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()

    var _dispatch = { }
    override fun dispatch(event: Event): Unit {
        mockFun.add("dispatch")
        mockData.add(listOf(event))
        return _dispatch()
    }
}
