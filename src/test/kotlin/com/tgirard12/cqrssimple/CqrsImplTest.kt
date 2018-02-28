package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.beLessThan
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.WordSpec
import org.junit.runner.RunWith


@RunWith(KTestJUnitRunner::class)
class CqrsImplTest : WordSpec() {

    private val queryBus = QueryBusStub()
    private val commandBus = CommandBusStub()
    private val eventBus = EventBusStub()
    private val middlewareBus = MiddlewareBusStub()

    private val cqrs = CqrsImpl(
            queryBus, commandBus, eventBus, middlewareBus
    )

    init {
        "CqrsImplTest" should {
            "dispatch query" {
                val query = AQuery()
                cqrs.query(query)

                queryBus.mockFun shouldEqual listOf("dispatch")
                queryBus.mockData shouldEqual listOf(listOf(query))

                commandBus.mockFun shouldEqual listOf<String>()
                eventBus.mockFun shouldEqual listOf<String>()
            }
            "dispatch command" {
                val command = ACommand()
                cqrs.command(command)

                commandBus.mockFun shouldEqual listOf("dispatch")
                commandBus.mockData shouldEqual listOf(listOf(command))

                queryBus.mockFun shouldEqual listOf<String>()
                eventBus.mockFun shouldEqual listOf<String>()
            }
            "dispatch event" {
                val event = AEvent()
                cqrs.event(event)

                eventBus.mockFun shouldEqual listOf("dispatch")
                eventBus.mockData shouldEqual listOf(listOf(event))

                queryBus.mockFun shouldEqual listOf<String>()
                commandBus.mockFun shouldEqual listOf<String>()
            }
        }
        "CqrsImplTest with middleware" should {
            "call preAction on Query" {
                cqrs.query(AQuery())
                middlewareBus.mockFun shouldEqual listOf("dispatch")
                middlewareBus.mockTime[0] should beLessThan(queryBus.mockTime[0])
            }
            "call postAction on Query" {
                cqrs.query(CQuery())
                middlewareBus.mockFun shouldEqual listOf("dispatch")
                middlewareBus.mockTime[0] should beGreaterThan(queryBus.mockTime[0])
            }
            "call pre-postAction on Query" {
                cqrs.query(ACQuery())
                middlewareBus.mockFun shouldEqual listOf("dispatch", "dispatch")
                middlewareBus.mockTime[0] should beLessThan(queryBus.mockTime[0])
                middlewareBus.mockTime[1] should beGreaterThan(queryBus.mockTime[0])
            }
            "call preAction on Command" {
                cqrs.command(ACommand())
                middlewareBus.mockFun shouldEqual listOf("dispatch")
                middlewareBus.mockTime[0] should beLessThan(commandBus.mockTime[0])
            }
            "call postAction on Command" {
                cqrs.command(CCommand())
                middlewareBus.mockFun shouldEqual listOf("dispatch")
                middlewareBus.mockTime[0] should beGreaterThan(commandBus.mockTime[0])
            }
            "call pre-postAction on Command" {
                cqrs.command(ACCommand())
                middlewareBus.mockFun shouldEqual listOf("dispatch", "dispatch")
                middlewareBus.mockTime[0] should beLessThan(commandBus.mockTime[0])
                middlewareBus.mockTime[1] should beGreaterThan(commandBus.mockTime[0])

            }
        }
    }
}
