package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger


/**
 * Query to dispatch
 */
interface Query {
    val name: String? get() = this::class.simpleName
}

/**
 *
 */
interface QueryBus {

    fun dispatch(query: Query): Any?
}

/**
 *
 */
@Suppress("AddVarianceModifier")
interface QueryHandler<Q : Query, out R> : Handler {
    fun handle(query: Q): R
}

/**
 *
 */
open class QueryHandlerBase<Q : Query, out R>(
        private val handleFun: ((Q) -> R)? = null
) : QueryHandler<Q, R> {

    override fun handle(query: Q): R = handleFun?.invoke(query)
            ?: throw IllegalArgumentException("handle Must be override or set via constructor")
}

/**
 *
 */
class QueryBusImpl(
        handlerList: List<QueryHandler<Query, Any>>
) : QueryBus {

    val log = getLogger("QueryBus")

    internal val handlers = handlerList.map {
        it.javaClass.kotlin.supertypes[0].arguments[0].type.toString() to it
    }.toMap()

    override fun dispatch(query: Query): Any? {
        val queryClass = query::class.qualifiedName
                ?: throw  IllegalArgumentException("Query ${query::class} ::class.qualifiedName NULL")
        val handler = handlers[queryClass]
                ?: throw  IllegalArgumentException("QueryHandler ${query::class} NULL")

        log.debug("${handler.name} > ${query.name}")
        return handler.handle(query)
    }
}

// Not work
//fun <Q : Query, R> queryHandler(f: (Q) -> R) =
//        object : QueryHandler<Q, R>(f) {}
