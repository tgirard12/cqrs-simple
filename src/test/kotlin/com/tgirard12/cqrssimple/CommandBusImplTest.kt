package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec


class CommandBusImplTest : WordSpec() {

    private val handlers = listOf(
        ACommandHandler(),
        BCommandHandler(),
        cCommandHandler
    )

    @Suppress("UNCHECKED_CAST")
    private val commandBus = CommandBusImpl(handlers as List<CommandHandler<Command<Any>, Any>>)

    override fun isInstancePerTest() = true

    init {
        "CommandBusImplTest" should {
            "handlers map" {
                commandBus.handlers shouldBe hashMapOf(
                    "com.tgirard12.cqrssimple.stub.ACommand" to handlers[0],
                    "com.tgirard12.cqrssimple.stub.BCommand" to handlers[1],
                    "com.tgirard12.cqrssimple.stub.CCommand" to handlers[2]
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
        }
    }
}
