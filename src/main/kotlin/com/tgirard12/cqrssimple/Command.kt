package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger

/**
 * Command to dispatch
 */
interface Command {
    val name: String? get() = this::class.simpleName
}

/**
 *
 */
interface CommandBus {

    fun dispatch(command: Command): Any?
}

/**
 *
 */
@Suppress("AddVarianceModifier")
interface CommandHandler<C : Command, out R> : Handler {
    fun handle(command: C): R
}

/**
 *
 */
open class CommandHandlerBase<C : Command, out R>(
        private val handleFun: ((C) -> R)? = null
) : CommandHandler<C, R> {

    override fun handle(command: C): R = handleFun?.invoke(command)
            ?: throw IllegalArgumentException("handle Must be override or set via constructor")
}

/**
 *
 */
class CommandBusImpl(
        handlerList: List<CommandHandler<Command, Any>>
) : CommandBus {

    val log = getLogger("CommandBus")

    internal val handlers = handlerList.map {
        it.javaClass.kotlin.supertypes[0].arguments[0].type.toString() to it
    }.toMap()

    override fun dispatch(command: Command): Any? {
        val queryClass = command::class.qualifiedName
                ?: throw  IllegalArgumentException("Command ${command::class} ::class.qualifiedName NULL")
        val handler = handlers[queryClass]
                ?: throw  IllegalArgumentException("CommandHandler ${command::class} NULL")

        log.debug("${handler.name} > ${command.name}")
        return handler.handle(command)
    }
}

// Not work
//fun <Q : Query, R> queryHandler(f: (Q) -> R) =
//        object : QueryHandler<Q, R>(f) {}
