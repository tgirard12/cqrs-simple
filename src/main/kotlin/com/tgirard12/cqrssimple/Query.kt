package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger


/**
 * Query to dispatch
 */
@Suppress("unused")
interface Query<R> : Clazz

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
fun interface QueryHandler<Q : Query<R>, R> : Clazz {
    fun handle(query: Q): R
}


/**
 *
 */
class QueryBusImpl : QueryBus {

    private val logger = getLogger(QueryBusImpl::class.java)

    internal val handlers = mutableMapOf<String, (Query<*>) -> Any>()

    @Suppress("UNCHECKED_CAST")
    fun <Q : Query<R>, R> register(handler: QueryHandler<Q, R>, className: String) {
        if (handlers.containsKey(className))
            throw java.lang.IllegalArgumentException("Query '$className' have already an Handler")
        else
            handlers[className] = { handler.handle(it as Q) as Any }
    }
    inline fun <reified Q : Query<R>, R> register(handler: QueryHandler<Q, R>) =
        register(handler, Q::class.java.clazzFullName)

    @Suppress("UNCHECKED_CAST")
    override fun <R> dispatch(query: Query<R>): R {
        val handler = handlers[query.clazzFullName]
                ?: throw  IllegalArgumentException("Query ${query.clazzFullName} have no QueryHandler")

        logger.debug("Invoke query ${query.clazzFullName}")
        return handler.invoke(query as Query<Any>) as R
    }
}
