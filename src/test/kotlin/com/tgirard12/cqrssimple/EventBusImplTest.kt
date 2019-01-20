package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.Description
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec


class EventBusImplTest : WordSpec() {

    private val handlers = listOf(
            AEventHandler(),
            BEventHandler(),
            cEventHandler,
            cEventHandler2
    )

    @Suppress("UNCHECKED_CAST")
    private val eventBus = EventBusImpl(handlers as List<EventHandler<Event>>)

    override fun isInstancePerTest() = true

    override fun beforeTest(description: Description) {
        EventCount.reset()
    }

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                eventBus.handlers shouldBe hashMapOf(
                    "com.tgirard12.cqrssimple.stub.AEvent" to listOf(handlers[0]),
                    "com.tgirard12.cqrssimple.stub.BEvent" to listOf(handlers[1]),
                    "com.tgirard12.cqrssimple.stub.CEvent" to listOf(handlers[2], handlers[3])
                )
            }
            "dispatch AEvent" {
                eventBus.dispatch(AEvent())
                EventCount.aCount shouldBe "a1"
                EventCount.bCount shouldBe "b"
                EventCount.cCount shouldBe "c"
                EventCount.dCount shouldBe "d"
            }
            "dispatch BEvent" {
                eventBus.dispatch(BEvent())
                EventCount.aCount shouldBe "a"
                EventCount.bCount shouldBe "b2"
                EventCount.cCount shouldBe "c"
                EventCount.dCount shouldBe "d"
            }
            "dispatch CEvent -> 2 events" {
                eventBus.dispatch(CEvent())
                EventCount.aCount shouldBe "a"
                EventCount.bCount shouldBe "b"
                EventCount.cCount shouldBe "c34"
                EventCount.dCount shouldBe "d"
            }
            "nothing on no EventHandler" {
                eventBus.dispatch(DEvent())
            }
        }
    }
}