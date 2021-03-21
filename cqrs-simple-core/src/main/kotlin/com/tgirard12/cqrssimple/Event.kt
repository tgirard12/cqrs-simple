package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger


/**
 *
 */
interface Event : Clazz

/**
 *
 */
@Suppress("AddVarianceModifier")
fun interface EventHandler<E : Event> : Clazz {

    fun handle(event: E): Unit
}

/**
 *
 */
//open class EventHandlerBase<E : Event>(
//        private val handleFun: ((E) -> Unit)? = null
//) : EventHandler<E> {
//
//    override fun handle(event: E): Unit = handleFun?.invoke(event)
//            ?: throw IllegalArgumentException("handle Must be override or set via constructor")
//}

/**
 *
 */
interface EventBus {
    fun dispatch(event: Event): Unit
}

/**
 *
 */
class EventBusImpl : EventBus {

    private val logger = getLogger(EventBusImpl::class.java)

    internal val handlers = mutableMapOf<String, MutableList<(Event) -> Unit>>()

    @Suppress("UNCHECKED_CAST")
    fun <E : Event> register(handler: EventHandler<E>, className: String) {
        if (handlers.containsKey(className))
            handlers[className]!!.add { handler.handle(it as E) }
        else
            handlers[className] = mutableListOf({ handler.handle(it as E) })
    }

    inline fun <reified E : Event> register(eventHandler: EventHandler<E>): Unit =
        register(eventHandler, E::class.java.clazzFullName)

    override fun dispatch(event: Event): Unit {
        handlers[event.clazzFullName]
            ?.let { evHandlers ->
                logger.debug("${evHandlers.size} eventHandlers found for '${event.clazzFullName}' Event")

                evHandlers.forEach { it.invoke(event) }
            }
            ?: logger.debug("No handler for '${event.clazzFullName}' Event")
    }

}

