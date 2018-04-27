package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.*


class ACommand : Command<String>, PreActionMiddleware
class BCommand : Command<Int>, PreActionMiddleware
class CCommand : Command<String>, PostActionMiddleware
class DCommand : Command<String>, PostActionMiddleware

class ACCommand : Command<String>, PreActionMiddleware, PostActionMiddleware

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

    var _dispatch: () -> Any? = { null }
    @Suppress("UNCHECKED_CAST")
    override fun <R> dispatch(command: Command<R>): R {
        mockFun.add("dispatch")
        mockData.add(listOf(command))
        mockTime.add(System.nanoTime())
        return _dispatch() as R
    }
}
