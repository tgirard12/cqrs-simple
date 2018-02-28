package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Middleware
import com.tgirard12.cqrssimple.MiddlewareBus
import com.tgirard12.cqrssimple.MiddlewareHandlerBase


class AMiddleware : Middleware
class BMiddleware : Middleware
class CMiddleware : Middleware
class DMiddleware : Middleware

object MiddlewareCount {
    fun reset() {
        aCount = "a"
        bCount = "b"
        cCount = "c"
        dCount = "d"
    }

    lateinit var aCount: String
    lateinit var bCount: String
    lateinit var cCount: String
    lateinit var dCount: String
}

class AMiddlewareHandler : MiddlewareHandlerBase<AMiddleware>() {
    override fun handle(middleware: AMiddleware): Unit {
        MiddlewareCount.aCount += "1"
    }
}

class BMiddlewareHandler : MiddlewareHandlerBase<BMiddleware>({
    MiddlewareCount.bCount += "2"
})

val cMiddlewareHandler = object : MiddlewareHandlerBase<CMiddleware>({
    MiddlewareCount.cCount += "3"
}) {}
val cMiddlewareHandler2 = object : MiddlewareHandlerBase<CMiddleware>({
    MiddlewareCount.cCount += "4"
}) {}

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class MiddlewareBusStub : MiddlewareBus, MockStub {

    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()

    var _dispatch = { Unit }
    override fun dispatch(middleware: Middleware) {
        mockFun.add("dispatch")
        mockData.add(listOf(middleware))
        return _dispatch()
    }
}
