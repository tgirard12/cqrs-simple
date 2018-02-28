package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger


/**
 *
 */
interface Middleware {
    val name: String? get() = this::class.simpleName
}

/**
 *
 */
interface MiddlewareBus {
    fun dispatch(middleware: Middleware): Unit
}

/**
 *
 */
@Suppress("AddVarianceModifier")
interface MiddlewareHandler<M : Middleware> : Handler {
    fun handle(middleware: M): Unit
}

/**
 *
 */
open class MiddlewareHandlerBase<M : Middleware>(
        private val handleFun: ((M) -> Unit)? = null
) : MiddlewareHandler<M> {
    override fun handle(middleware: M) = handleFun?.invoke(middleware)
            ?: throw IllegalArgumentException("handle Must be override or set via constructor")
}

/**
 *
 */
class MiddlewareBusImpl(
        handlerList: List<MiddlewareHandler<Middleware>>
) : MiddlewareBus {

    val log = getLogger("MiddlewareBus")

    internal val handlers = handlerList
            .map {
                it.javaClass.kotlin.supertypes[0].arguments[0].type.toString() to it
            }
            .groupBy({ it.first }, { it.second })

    override fun dispatch(middleware: Middleware) {
        val middlewareClass = middleware::class.qualifiedName
                ?: throw  IllegalArgumentException("Middleware ${middleware::class} ::class.qualifiedName NULL")
        handlers[middlewareClass]
                ?.let {
                    it.forEach {
                        log.debug("${it.name} > ${middleware.name}")
                        it.handle(middleware)
                    }
                }
                ?: log.info("No handler for '${middleware.name}' Middleware")

    }
}
















