package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.*


class ACommand : Command<String>
class BCommand : Command<Int>, AMiddleware
class CCommand : Command<String>, BMiddleware, AMiddleware, DMiddleware
class DCommand : Command<String>, BMiddleware

class ACommandHandler : CommandHandler<ACommand, String> {
    override fun handle(command: ACommand): String = "ACommandHandler"
}

class BCommandHandler : CommandHandler<BCommand, Int> {
    override fun handle(command: BCommand): Int = 1234
}

val cCommandHandler = CommandHandler<CCommand, String> { "CCommandHandler" }
val cCommandHandler2 = CommandHandler<CCommand, String> { "CCommandHandler2" }
