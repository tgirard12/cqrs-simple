package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.WordSpec
import org.junit.runner.RunWith


@Suppress("UNCHECKED_CAST")
@RunWith(KTestJUnitRunner::class)
class CommandBusImplTest : WordSpec() {

    private val handlers = listOf(
            ACommandHandler(),
            BCommandHandler(),
            cCommandHandler
    )
    private val commandBus = CommandBusImpl(handlers as List<CommandHandler<Command, Any>>)

    override val oneInstancePerTest: Boolean = true

    init {
        "CommandBusImplTest" should {
            "handlers map" {
                commandBus.handlers shouldEqual hashMapOf(
                        "com.tgirard12.cqrssimple.stub.ACommand" to handlers[0],
                        "com.tgirard12.cqrssimple.stub.BCommand" to handlers[1],
                        "com.tgirard12.cqrssimple.stub.CCommand" to handlers[2]
                )
            }
            "dispatch ACommand" {
                commandBus.dispatch(ACommand()) shouldEqual "ACommandHandler"
            }
            "dispatch BCommand" {
                commandBus.dispatch(BCommand()) shouldEqual 1234
            }
            "dispatch CCommand" {
                commandBus.dispatch(CCommand()) shouldEqual "CCommandHandler"
            }
            "fail no CommandHandler" {
                shouldThrow<IllegalArgumentException> {
                    commandBus.dispatch(DCommand())
                }
            }
        }
    }
}
