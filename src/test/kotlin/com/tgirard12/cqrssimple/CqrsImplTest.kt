package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe


class CqrsImplTest : WordSpec() {

    private lateinit var queryBus: QueryBus
    private lateinit var commandBus: CommandBus
    private lateinit var eventBus: EventBus
    private lateinit var middlewareBus: MiddlewareBus
    private lateinit var cqrs: CqrsImpl

    override fun beforeEach(testCase: TestCase) {
        queryBus = QueryBusImpl().apply {
            register(AQueryHandler())
            register(BQueryHandler)
            register(cQueryHandler)
        }
        commandBus = CommandBusImpl().apply {
            register(ACommandHandler())
            register(BCommandHandler())
            register(cCommandHandler)
        }
        eventBus = EventBusImpl().apply {
            register(AEventHandler())
            register(BEventHandler())
            register(cEventHandler)
            register(cEventHandler2)
        }
        middlewareBus = MiddlewareBusImpl().apply {
            register(AMiddlewareHandler())
            register(BMiddlewareHandler)
            register(cMiddlewareHandler)
            register(dMiddlewareHandler)
        }
        cqrs = CqrsImpl(queryBus, commandBus, eventBus, middlewareBus)

        EventCount.reset()
        MiddlewareCount.reset()
    }

    init {
        "CqrsImpl Query" should {
            "dispatch query" {
                cqrs.query(AQuery()) shouldBe "AQueryHandler"
                MiddlewareCount.sequence shouldBe ""
            }
            "dispatch query and preMiddleware" {
                cqrs.query(BQuery()) shouldBe 3456
                MiddlewareCount.sequence shouldBe "a1"
            }
            "dispatch query pre and postMiddleware" {
                cqrs.query(CQuery()) shouldBe "CQueryHandler"
                MiddlewareCount.sequence shouldBe "a1b2d5"
            }
        }
        "CqrsImpl Command" should {
            "dispatch command" {
                cqrs.command(ACommand()) shouldBe "ACommandHandler"
                MiddlewareCount.sequence shouldBe ""
            }
            "dispatch query and preMiddleware" {
                cqrs.command(BCommand()) shouldBe 1234
                MiddlewareCount.sequence shouldBe "a1"
            }
            "dispatch query pre and postMiddleware" {
                cqrs.command(CCommand()) shouldBe "CCommandHandler"
                MiddlewareCount.sequence shouldBe "a1b2d5"
            }
        }
        "CqrsImpl Event" should {
            "dispatch event" {
                cqrs.event(AEvent())

                MiddlewareCount.sequence shouldBe ""
                EventCount.sequence shouldBe "a1"
            }
        }
    }
}
