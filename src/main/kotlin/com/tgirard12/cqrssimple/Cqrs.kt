package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger

interface Cqrs {

    val commandBus: CommandBus
    val queryBus: QueryBus
    val eventBus: EventBus
    val middlewareBus: MiddlewareBus

    fun command(command: Command): Any?
    fun query(query: Query): Any?
    fun event(event: Event)
}

interface DescName {
    val name: String? get() = this::class.simpleName
}

class CqrsImpl(
        override val queryBus: QueryBus,
        override val commandBus: CommandBus,
        override val eventBus: EventBus,
        override val middlewareBus: MiddlewareBus
) : Cqrs {

    val log = getLogger("cqrs")

    override fun command(command: Command): Any? {
        log.debug("dispatch command > ${command.name}")

        if (command is PreActionMiddleware)
            middlewareBus.dispatch(command)

        val comVal = commandBus.dispatch(command)

        if (command is PostActionMiddleware)
            middlewareBus.dispatch(command)

        return comVal
    }

    override fun query(query: Query): Any? {
        log.debug("dispatch query > ${query.name}")

        if (query is PreActionMiddleware)
            middlewareBus.dispatch(query)

        val comVal = queryBus.dispatch(query)

        if (query is PostActionMiddleware)
            middlewareBus.dispatch(query)

        return comVal

    }

    override fun event(event: Event) {
        log.debug("dispatch event > ${event.name}")
        eventBus.dispatch(event)
    }
}
