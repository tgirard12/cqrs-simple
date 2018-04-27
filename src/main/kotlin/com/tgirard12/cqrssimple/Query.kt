package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger


/**
 * Query to dispatch
 */
@Suppress("unused")
interface Query<R> : DescName

/**
 *
 */
interface QueryBus {

    fun <R> dispatch(query: Query<R>): R
}

/**
 *
 */
@Suppress("AddVarianceModifier")
interface QueryHandler<Q : Query<R>, R> : DescName {
    fun handle(query: Q): R
}

/**
 *
 */
open class QueryHandlerBase<Q : Query<R>, R>(
        private val handleFun: ((Q) -> R)? = null
) : QueryHandler<Q, R> {

    override fun handle(query: Q): R = handleFun?.invoke(query)
            ?: throw IllegalArgumentException("handle Must be override or set via constructor")

//    Not working du to type erasure
//    fun invoke(f: (((Q) -> R)?)): QueryHandlerBase<Q, R> =
//            object : QueryHandlerBase<Q, R>(f) {}
}

/**
 *
 */
class QueryBusImpl(
        handlerList: List<QueryHandler<Query<Any>, Any>>
) : QueryBus {

    private val log = getLogger("QueryBus")

    internal val handlers = handlerList.map {
        it.javaClass.kotlin.supertypes[0].arguments[0].type.toString() to it
    }.toMap()

    @Suppress("UNCHECKED_CAST")
    override fun <R> dispatch(query: Query<R>): R {
        val queryClass = query::class.qualifiedName
                ?: throw  IllegalArgumentException("Query ${query::class} ::class.qualifiedName NULL")
        val handler = handlers[queryClass]
                ?: throw  IllegalArgumentException("QueryHandler ${query::class} NULL")

        log.debug("${handler.name} > ${query.name}")
        return handler.handle(query as Query<Any>) as R
    }
}

// Not work
//fun <Q : Query, R> queryHandler(f: (Q) -> R) =
//        object : QueryHandler<Q, R>(f) {}
