package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger

/**
 * Command to dispatch
 */
@Suppress("unused")
interface Command<R> : Clazz

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
fun interface CommandHandler<C : Command<R>, R> : Clazz {
    fun handle(command: C): R
}

/**
 *
 */
class CommandBusImpl : CommandBus {

    private val logger = getLogger(CommandBusImpl::class.java)

    internal val handlers = mutableMapOf<String, (Command<*>) -> Any>()

    @Suppress("UNCHECKED_CAST")
    fun <C : Command<*>> register(handler: CommandHandler<C, *>, className: String) {
        if (handlers.containsKey(className))
            throw java.lang.IllegalArgumentException("Command '$className' have already an Handler")
        else
            handlers[className] = { handler.handle(it as C) as Any }
    }
    inline fun <reified C : Command<*>> register(handler: CommandHandler<C, *>) =
        register(handler, C::class.java.clazzFullName)


    @Suppress("UNCHECKED_CAST")
    override fun <R> dispatch(command: Command<R>): R {
        if (handlers.containsKey(command.clazzFullName).not())
            throw  IllegalArgumentException("Command ${command.clazzFullName} not registered")

        logger.debug("Command ${command.clazzFullName} dispatch")
        return handlers[command.clazzFullName]!!.invoke(command) as R
    }
}
