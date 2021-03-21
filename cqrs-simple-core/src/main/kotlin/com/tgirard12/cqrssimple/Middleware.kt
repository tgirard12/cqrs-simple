package com.tgirard12.cqrssimple

import org.slf4j.LoggerFactory.getLogger
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

/**
 *
 */
interface MiddlewareAction : Clazz

/**
 *
 */
interface PreActionMiddleware : MiddlewareAction

/**
 *
 */
interface PostActionMiddleware : MiddlewareAction

/**
 *
 */
interface MiddlewareBus {
    fun dispatchPreAction(preAction: PreActionMiddleware): Unit
    fun dispatchPostAction(postAction: PostActionMiddleware): Unit
}

/**
 *
 */
@Suppress("AddVarianceModifier")
fun interface MiddlewareHandler<M : MiddlewareAction> : Clazz {
    fun handle(middleware: M): Unit
}

/**
 *
 */
class MiddlewareBusImpl : MiddlewareBus {

    val logger = getLogger("MiddlewareBus")
    internal val preHandlers = mutableMapOf<KClass<*>, (PreActionMiddleware) -> Unit>()
    internal val postHandlers = mutableMapOf<KClass<*>, (PostActionMiddleware) -> Unit>()

    @Suppress("UNCHECKED_CAST")
    fun <M : MiddlewareAction> register(handler: MiddlewareHandler<M>, kClass: KClass<*>) {
        if (kClass.isSubclassOf(PreActionMiddleware::class)) {
            if (preHandlers.containsKey(kClass))
                throw java.lang.IllegalArgumentException("Middleware '$kClass' have already register")
            else
                preHandlers[kClass] = { handler.handle(it as M) }
        }
        if (kClass.isSubclassOf(PostActionMiddleware::class)) {
            if (postHandlers.containsKey(kClass))
                throw java.lang.IllegalArgumentException("Middleware '$kClass' have already register")
            else
                postHandlers[kClass] = { handler.handle(it as M) }
        }
    }

    inline fun <reified M : MiddlewareAction> register(handler: MiddlewareHandler<M>) =
        register(handler, M::class)


    override fun dispatchPreAction(preAction: PreActionMiddleware) {
        preAction::class.supertypes
            .forEach { type ->
                preHandlers[type.jvmErasure]
                    ?.let {
                        logger.debug("invoke middleware for ${preAction.clazzFullName}")
                        it.invoke(preAction)
                    }
            }
    }

    override fun dispatchPostAction(postAction: PostActionMiddleware) {
        postAction::class.supertypes
            .forEach { type ->
                postHandlers[type.jvmErasure]
                    ?.let {
                        logger.debug("invoke middleware for ${postAction.clazzFullName}")
                        it.invoke(postAction)
                    }
            }
    }
}
















