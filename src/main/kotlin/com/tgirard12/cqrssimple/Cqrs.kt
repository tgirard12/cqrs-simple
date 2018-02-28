package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger

interface Cqrs {

    val commandBus: CommandBus
    val queryBus: QueryBus
    val eventBus: EventBus
    val middlewareBus: MiddlewareBus

    fun command(command: Command)
    fun query(query: Query)
    fun event(event: Event)
}

interface Handler {
    val name: String? get() = this::class.simpleName
}

class CqrsImpl(
        override val queryBus: QueryBus,
        override val commandBus: CommandBus,
        override val eventBus: EventBus,
        override val middlewareBus: MiddlewareBus
) : Cqrs {

    val log = getLogger("cqrs")

    override fun command(command: Command) {
        log.debug("dispatch command > ${command.name}")
        commandBus.dispatch(command)
    }

    override fun query(query: Query) {
        log.debug("dispatch query > ${query.name}")
        queryBus.dispatch(query)
    }

    override fun event(event: Event) {
        log.debug("dispatch event > ${event.name}")
        eventBus.dispatch(event)
    }
}
