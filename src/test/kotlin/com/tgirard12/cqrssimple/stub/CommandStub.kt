package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Command
import com.tgirard12.cqrssimple.CommandBus
import com.tgirard12.cqrssimple.CommandHandlerBase


class ACommand : Command
class BCommand : Command
class CCommand : Command
class DCommand : Command


class ACommandHandler : CommandHandlerBase<ACommand, String>() {
    override fun handle(command: ACommand): String = "ACommandHandler"
}

class BCommandHandler : CommandHandlerBase<BCommand, Int>({ 1234 })

val cCommandHandler = object : CommandHandlerBase<CCommand, String>({ "CCommandHandler" }) {}


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class CommandBusStub : CommandBus, MockStub {
    override val mockFun: MutableList<String> = mutableListOf()
    override val mockData: MutableList<Any> = mutableListOf()

    var _dispatch = { null }
    override fun dispatch(command: Command): Any? {
        mockFun.add("dispatch")
        mockData.add(listOf(command))
        return _dispatch()
    }
}
