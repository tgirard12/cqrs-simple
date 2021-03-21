package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Query
import com.tgirard12.cqrssimple.QueryHandler

class AQuery : Query<String>
class BQuery : Query<Int>, AMiddleware
class CQuery : Query<String>, BMiddleware, AMiddleware, DMiddleware
class DQuery : Query<String>, BMiddleware

class ACQuery : Query<Int>, AMiddleware, BMiddleware

class AQueryHandler : QueryHandler<AQuery, String> {
    override fun handle(query: AQuery): String = "AQueryHandler"
}

object BQueryHandler : QueryHandler<BQuery, Int> {
    override fun handle(query: BQuery): Int = 3456
}

val cQueryHandler = QueryHandler<CQuery, String> { "CQueryHandler" }
val cQueryHandler2 = QueryHandler<CQuery, String> { "CQueryHandler2" }
