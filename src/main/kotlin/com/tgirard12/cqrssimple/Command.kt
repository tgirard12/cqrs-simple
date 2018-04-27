package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger

/**
 * Command to dispatch
 */
@Suppress("unused")
interface Command<R> : DescName

/**
 *
 */
interface CommandBus {

    fun <R> dispatch(command: Command<R>): R
}

/**
 *
 */
@Suppress("AddVarianceModifier")
interface CommandHandler<C : Command<R>, R> : DescName {
    fun handle(command: C): R
}

/**
 *
 */
open class CommandHandlerBase<C : Command<R>, R>(
        private val handleFun: ((C) -> R)? = null
) : CommandHandler<C, R> {

    override fun handle(command: C): R = handleFun?.invoke(command)
            ?: throw IllegalArgumentException("handle Must be override or set via constructor")
}

/**
 *
 */
class CommandBusImpl(
        handlerList: List<CommandHandler<Command<Any>, Any>>
) : CommandBus {

    private val log = getLogger("CommandBus")

    internal val handlers = handlerList.map {
        it.javaClass.kotlin.supertypes[0].arguments[0].type.toString() to it
    }.toMap()

    @Suppress("UNCHECKED_CAST")
    override fun <R> dispatch(command: Command<R>): R {
        val queryClass = command::class.qualifiedName
                ?: throw  IllegalArgumentException("Command ${command::class} ::class.qualifiedName NULL")
        val handler = handlers[queryClass]
                ?: throw  IllegalArgumentException("CommandHandler ${command::class} NULL")

        log.debug("${handler.name} > ${command.name}")
        return handler.handle(command as Command<Any>) as R
    }
}

// Not work
//fun <Q : Query, R> queryHandler(f: (Q) -> R) =
//        object : QueryHandler<Q, R>(f) {}
