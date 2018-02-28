package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger


/**
 *
 */
interface Event : DescName

/**
 *
 */
interface EventBus {
    fun dispatch(event: Event): Unit
}

/**
 *
 */
@Suppress("AddVarianceModifier")
interface EventHandler<E : Event> : DescName {
    fun handle(event: E): Unit
}

/**
 *
 */
open class EventHandlerBase<E : Event>(
        private val handleFun: ((E) -> Unit)? = null
) : EventHandler<E> {

    override fun handle(event: E): Unit = handleFun?.invoke(event)
            ?: throw IllegalArgumentException("handle Must be override or set via constructor")
}

/**
 *
 */
class EventBusImpl(
        handlerList: List<EventHandler<Event>>
) : EventBus {

    val log = getLogger("EventBus")

    internal val handlers = handlerList
            .map {
                it.javaClass.kotlin.supertypes[0].arguments[0].type.toString() to it
            }
            .groupBy({ it.first }, { it.second })

    override fun dispatch(event: Event): Unit {
        val eventClass = event::class.qualifiedName
                ?: throw  IllegalArgumentException("Event ${event::class} ::class.qualifiedName NULL")

        handlers[eventClass]
                ?.let {
                    it.forEach {
                        log.debug("${it.name} > ${event.name}")
                        it.handle(event)
                    }
                }
                ?: log.info("No handler for '${event.name}' Event")
    }
}
