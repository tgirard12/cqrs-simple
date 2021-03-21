package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.MiddlewareHandler
import com.tgirard12.cqrssimple.PostActionMiddleware
import com.tgirard12.cqrssimple.PreActionMiddleware


interface AMiddleware : PreActionMiddleware
interface BMiddleware : PostActionMiddleware
interface CMiddleware : PreActionMiddleware
interface DMiddleware : PostActionMiddleware
interface EMiddleware : PostActionMiddleware

object MiddlewareCount {
    lateinit var sequence: String

    fun add(id: String) {
        sequence += id
    }

    fun reset() {
        sequence = ""
    }
}

class AMiddlewareHandler : MiddlewareHandler<AMiddleware> {
    override fun handle(middleware: AMiddleware): Unit {
        MiddlewareCount.add("a1")
    }
}

object BMiddlewareHandler : MiddlewareHandler<BMiddleware> {
    override fun handle(middleware: BMiddleware) {
        MiddlewareCount.add("b2")
    }
}

val cMiddlewareHandler = MiddlewareHandler<CMiddleware> { MiddlewareCount.add("c") }
val cMiddlewareHandler2 = MiddlewareHandler<CMiddleware> { MiddlewareCount.add("C4") }
val dMiddlewareHandler = MiddlewareHandler<DMiddleware> { MiddlewareCount.add("d5") }
