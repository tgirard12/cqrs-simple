package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.WordSpec
import org.junit.runner.RunWith


@RunWith(KTestJUnitRunner::class)
class CqrsImplTest : WordSpec() {

    val queryBus = QueryBusStub()
    val commandBus = CommandBusStub()
    val eventBus = EventBusStub()
    val middlewareBus = MiddlewareBusStub()

    val cqrs = CqrsImpl(
            queryBus, commandBus, eventBus, middlewareBus
    )

    init {
        "CqrsImplTest" should {
            "dispatch query" {
                val query = AQuery()
                cqrs.query(query)

                queryBus.mockFun shouldEqual listOf("dispatch")
                queryBus.mockData shouldEqual listOf(listOf(query))
            }
            "dispatch command" {
                val command = ACommand()
                cqrs.command(command)

                commandBus.mockFun shouldEqual listOf("dispatch")
                commandBus.mockData shouldEqual listOf(listOf(command))
            }
            "dispatch event" {
                val event = AEvent()
                cqrs.event(event)

                eventBus.mockFun shouldEqual listOf("dispatch")
                eventBus.mockData shouldEqual listOf(listOf(event))
            }
        }
    }
}
