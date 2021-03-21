package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe


class CommandBusImplTest : WordSpec() {

    lateinit var commandBus: CommandBusImpl

    override fun beforeEach(testCase: TestCase) {
        commandBus = CommandBusImpl().apply {
            register(ACommandHandler())
            register(BCommandHandler())
            register(cCommandHandler)
        }
    }

    init {
        "CommandBusImplTest" should {
            "handlers map" {
                commandBus.handlers.keys shouldBe setOf(
                    "com.tgirard12.cqrssimple.stub.ACommand",
                    "com.tgirard12.cqrssimple.stub.BCommand",
                    "com.tgirard12.cqrssimple.stub.CCommand",
                )
            }
            "dispatch ACommand" {
                commandBus.dispatch(ACommand()) shouldBe "ACommandHandler"
            }
            "dispatch BCommand" {
                commandBus.dispatch(BCommand()) shouldBe 1234
            }
            "dispatch CCommand" {
                commandBus.dispatch(CCommand()) shouldBe "CCommandHandler"
            }
            "fail no CommandHandler" {
                shouldThrow<IllegalArgumentException> {
                    commandBus.dispatch(DCommand())
                }
            }
            "fail no register twice same command" {
                shouldThrow<IllegalArgumentException> {
                    commandBus.register(cCommandHandler2)
                }
            }
        }
    }
}
