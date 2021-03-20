package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe


class EventBusImplTest : WordSpec() {

    @Suppress("UNCHECKED_CAST")
    lateinit var eventBus : EventBusImpl

    override fun beforeEach(testCase: TestCase) {
        EventCount.reset()
        eventBus = EventBusImpl().apply {
            register(AEventHandler())
            register(BEventHandler())
            register(cEventHandler)
            register(cEventHandler2)
        }
    }

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                eventBus.handlers.keys shouldBe setOf(
                    "com.tgirard12.cqrssimple.stub.AEvent",
                    "com.tgirard12.cqrssimple.stub.BEvent",
                    "com.tgirard12.cqrssimple.stub.CEvent"
                )
                eventBus.handlers["com.tgirard12.cqrssimple.stub.CEvent"]!! shouldHaveSize  2
            }
            "dispatch AEvent" {
                eventBus.dispatch(AEvent())
                EventCount.sequence shouldBe "a1"
            }
            "dispatch BEvent" {
                eventBus.dispatch(BEvent())
                EventCount.sequence shouldBe "b2"
            }
            "dispatch CEvent -> 2 events" {
                eventBus.dispatch(CEvent())
                EventCount.sequence shouldBe "c3C4"
            }
            "nothing on no EventHandler" {
                eventBus.dispatch(DEvent())
            }
        }
    }
}
