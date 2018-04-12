package com.tgirard12.cqrssimple.reactor

import reactor.core.publisher.Mono

interface CqrsReactor {

    fun <R> query(query: QueryReactor<R>): Mono<R>

}

class CqrsReactorImpl {

    val queries: List<(QueryReactor<*>) -> *> = listOf()


}