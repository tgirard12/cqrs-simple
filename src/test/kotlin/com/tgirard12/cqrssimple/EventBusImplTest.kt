package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.TestCaseContext
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.WordSpec
import org.junit.runner.RunWith


@Suppress("UNCHECKED_CAST")
@RunWith(KTestJUnitRunner::class)
class EventBusImplTest : WordSpec() {

    private val handlers = listOf(
            AEventHandler(),
            BEventHandler(),
            cEventHandler,
            cEventHandler2
    )
    private val eventBus = EventBusImpl(handlers as List<EventHandler<Event>>)

    override val oneInstancePerTest: Boolean = true

    override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        EventCount.reset()
        test()
    }

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                eventBus.handlers shouldEqual hashMapOf(
                        "com.tgirard12.cqrssimple.stub.AEvent" to listOf(handlers[0]),
                        "com.tgirard12.cqrssimple.stub.BEvent" to listOf(handlers[1]),
                        "com.tgirard12.cqrssimple.stub.CEvent" to listOf(handlers[2], handlers[3])
                )
            }
            "dispatch AEvent" {
                eventBus.dispatch(AEvent())
                EventCount.aCount shouldEqual "a1"
                EventCount.bCount shouldEqual "b"
                EventCount.cCount shouldEqual "c"
                EventCount.dCount shouldEqual "d"
            }
            "dispatch BEvent" {
                eventBus.dispatch(BEvent())
                EventCount.aCount shouldEqual "a"
                EventCount.bCount shouldEqual "b2"
                EventCount.cCount shouldEqual "c"
                EventCount.dCount shouldEqual "d"
            }
            "dispatch CEvent -> 2 events" {
                eventBus.dispatch(CEvent())
                EventCount.aCount shouldEqual "a"
                EventCount.bCount shouldEqual "b"
                EventCount.cCount shouldEqual "c34"
                EventCount.dCount shouldEqual "d"
            }
            "nothing on no EventHandler" {
                eventBus.dispatch(DEvent())
            }
        }
    }
}