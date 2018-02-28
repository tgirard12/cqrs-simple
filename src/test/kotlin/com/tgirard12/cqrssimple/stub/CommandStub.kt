package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.*


class ACommand : Command, PreActionMiddleware
class BCommand : Command, PreActionMiddleware
class CCommand : Command, PostActionMiddleware
class DCommand : Command, PostActionMiddleware

class ACCommand : Command, PreActionMiddleware, PostActionMiddleware

class ACommandHandler : CommandHandlerBase<ACommand, String>() {
    override fun handle(command: ACommand): String = "ACommandHandler"
}

class BCommandHandler : CommandHandlerBase<BCommand, Int>({ 1234 })

val cCommandHandler = object : CommandHandlerBase<CCommand, String>({ "CCommandHandler" }) {}


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class CommandBusStub : CommandBus, MockStub {
    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()
    override val mockTime: MutableList<Long> = mutableListOf()

    var _dispatch = { null }
    override fun dispatch(command: Command): Any? {
        mockFun.add("dispatch")
        mockData.add(listOf(command))
        mockTime.add(System.nanoTime())
        return _dispatch()
    }
}
