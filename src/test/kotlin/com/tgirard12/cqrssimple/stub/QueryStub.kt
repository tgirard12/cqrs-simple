package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Query
import com.tgirard12.cqrssimple.QueryBus
import com.tgirard12.cqrssimple.QueryHandlerBase

class AQuery : Query
class BQuery : Query
class CQuery : Query
class DQuery : Query

class AQueryHandler : QueryHandlerBase<AQuery, String>() {
    override fun handle(query: AQuery): String = "AQueryHandler"
}

class BQueryHandler : QueryHandlerBase<BQuery, Int>({ 3456 })

val cQueryHandler = object : QueryHandlerBase<CQuery, String>({ "BQueryHandler" }) {}


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class QueryBusStub : QueryBus, MockStub {
    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()

    var _dispatch = { null }
    override fun dispatch(query: Query): Any? {
        mockFun.add("dispatch")
        mockData.add(listOf(query))
        return _dispatch()
    }
}
