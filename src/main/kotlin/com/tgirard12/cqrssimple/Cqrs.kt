package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger

interface Cqrs {

    val commandBus: CommandBus
    val queryBus: QueryBus
    val eventBus: EventBus
    val middlewareBus: MiddlewareBus

    fun <R> command(command: Command<R>): Any?
    fun <R> query(query: Query<R>): R?
    fun event(event: Event)
}

interface Clazz {
    val label: String
        get() = javaClass.simpleName
    val clazzFullName: String
        get() = javaClass.name
}

val Class<*>.clazzFullName: String
    get() = name
val Class<*>.label: String
    get() = simpleName

class CqrsImpl(
    override val queryBus: QueryBus,
    override val commandBus: CommandBus,
    override val eventBus: EventBus,
    override val middlewareBus: MiddlewareBus
) : Cqrs {

    val log = getLogger("cqrs")

    override fun <R> command(command: Command<R>): R {
        log.debug("dispatch command > ${command.clazzFullName}")

        if (command is PreActionMiddleware)
            middlewareBus.dispatchPreAction(command)

        val comVal = commandBus.dispatch(command)

        if (command is PostActionMiddleware)
            middlewareBus.dispatchPostAction(command)

        return comVal
    }

    override fun <R> query(query: Query<R>): R {
        log.debug("dispatch query > ${query.clazzFullName}")

        if (query is PreActionMiddleware)
            middlewareBus.dispatchPreAction(query)

        val comVal = queryBus.dispatch(query)

        if (query is PostActionMiddleware)
            middlewareBus.dispatchPostAction(query)

        return comVal
    }

    override fun event(event: Event) {
        log.debug("dispatch event > ${event.clazzFullName}")
        eventBus.dispatch(event)
    }
}
