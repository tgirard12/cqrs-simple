package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.matchers.numerics.shouldBeLessThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec


class CqrsImplTest : WordSpec() {

    private val queryBus = QueryBusStub()
    private val commandBus = CommandBusStub()
    private val eventBus = EventBusStub()
    private val middlewareBus = MiddlewareBusStub()

    private val cqrs = CqrsImpl(
        queryBus, commandBus, eventBus, middlewareBus
    )

    override fun isInstancePerTest() = true

    init {
        "CqrsImplTest" should {
            "dispatch query" {
                val query = AQuery()
                cqrs.query(query)

                queryBus.mockFun shouldBe listOf("dispatch")
                queryBus.mockData shouldBe listOf(listOf(query))

                commandBus.mockFun shouldBe listOf<String>()
                eventBus.mockFun shouldBe listOf<String>()
            }
            "return query result" {
                queryBus._dispatch = { "queryResult" }
                cqrs.query(AQuery()) shouldBe "queryResult"
            }
            "dispatch command" {
                val command = ACommand()
                cqrs.command(command)

                commandBus.mockFun shouldBe listOf("dispatch")
                commandBus.mockData shouldBe listOf(listOf(command))

                queryBus.mockFun shouldBe listOf<String>()
                eventBus.mockFun shouldBe listOf<String>()
            }
            "return command result" {
                commandBus._dispatch = { "commandResult" }
                cqrs.command(ACommand()) shouldBe "commandResult"
            }
            "dispatch event" {
                val event = AEvent()
                cqrs.event(event)

                eventBus.mockFun shouldBe listOf("dispatch")
                eventBus.mockData shouldBe listOf(listOf(event))

                queryBus.mockFun shouldBe listOf<String>()
                commandBus.mockFun shouldBe listOf<String>()
            }
        }
        "CqrsImplTest with middleware" should {
            "call preAction on Query" {
                cqrs.query(AQuery())
                middlewareBus.mockFun shouldBe listOf("dispatch")
                middlewareBus.mockTime[0] shouldBeLessThan queryBus.mockTime[0]
            }
            "call postAction on Query" {
                cqrs.query(CQuery())
                middlewareBus.mockFun shouldBe listOf("dispatch")
                middlewareBus.mockTime[0] shouldBeGreaterThan queryBus.mockTime[0]
            }
            "call pre-postAction on Query" {
                cqrs.query(ACQuery())
                middlewareBus.mockFun shouldBe listOf("dispatch", "dispatch")
                middlewareBus.mockTime[0] shouldBeLessThan queryBus.mockTime[0]
                middlewareBus.mockTime[1] shouldBeGreaterThan queryBus.mockTime[0]
            }
            "call preAction on Command" {
                cqrs.command(ACommand())
                middlewareBus.mockFun shouldBe listOf("dispatch")
                middlewareBus.mockTime[0] shouldBeLessThan commandBus.mockTime[0]
            }
            "call postAction on Command" {
                cqrs.command(CCommand())
                middlewareBus.mockFun shouldBe listOf("dispatch")
                middlewareBus.mockTime[0] shouldBeGreaterThan commandBus.mockTime[0]
            }
            "call pre-postAction on Command" {
                cqrs.command(ACCommand())
                middlewareBus.mockFun shouldBe listOf("dispatch", "dispatch")
                middlewareBus.mockTime[0] shouldBeLessThan commandBus.mockTime[0]
                middlewareBus.mockTime[1] shouldBeGreaterThan commandBus.mockTime[0]

            }
        }
    }
}
