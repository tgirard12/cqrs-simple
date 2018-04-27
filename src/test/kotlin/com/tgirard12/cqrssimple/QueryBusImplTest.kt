package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.WordSpec
import org.junit.runner.RunWith


@Suppress("UNCHECKED_CAST")
@RunWith(KTestJUnitRunner::class)
class QueryBusImplTest : WordSpec() {

    private val handlers = listOf(
            AQueryHandler(),
            BQueryHandler(),
            cQueryHandler
    )
    private val queryBus = QueryBusImpl(handlers as List<QueryHandler<Query<Any>, Any>>)

    override val oneInstancePerTest: Boolean = true

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                queryBus.handlers shouldEqual hashMapOf(
                        "com.tgirard12.cqrssimple.stub.AQuery" to handlers[0],
                        "com.tgirard12.cqrssimple.stub.BQuery" to handlers[1],
                        "com.tgirard12.cqrssimple.stub.CQuery" to handlers[2]
                )
            }
            "dispatch Aquery" {
                queryBus.dispatch(AQuery()) shouldEqual "AQueryHandler"
            }
            "dispatch Bquery" {
                queryBus.dispatch(BQuery()) shouldEqual 3456
            }
            "dispatch Cquery" {
                queryBus.dispatch(CQuery()) shouldEqual "BQueryHandler"
            }
            "fail no QueryHandler" {
                shouldThrow<IllegalArgumentException> {
                    queryBus.dispatch(DQuery())
                }
            }
        }
    }
}
