package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.*

class AQuery : Query<String>, PreActionMiddleware
class BQuery : Query<Int>, PreActionMiddleware
class CQuery : Query<String>, PostActionMiddleware
class DQuery : Query<String>, PostActionMiddleware

class ACQuery : Query<Int>, PreActionMiddleware, PostActionMiddleware

class AQueryHandler : QueryHandlerBase<AQuery, String>() {
    override fun handle(query: AQuery): String = "AQueryHandler"
}

class BQueryHandler : QueryHandlerBase<BQuery, Int>({ 3456 })

val cQueryHandler = object : QueryHandlerBase<CQuery, String>({ "BQueryHandler" }) {}


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class QueryBusStub : QueryBus, MockStub {
    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()
    override val mockTime: MutableList<Long> = mutableListOf()

    var _dispatch: () -> Any? = { null }
    @Suppress("UNCHECKED_CAST")
    override fun <R> dispatch(query: Query<R>): R {
        mockFun.add("dispatch")
        mockData.add(listOf(query))
        mockTime.add(System.nanoTime())
        return _dispatch() as R
    }
}
