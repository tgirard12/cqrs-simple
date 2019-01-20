package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec


class QueryBusImplTest : WordSpec() {

    private val handlers = listOf(
            AQueryHandler(),
            BQueryHandler(),
            cQueryHandler
    )

    @Suppress("UNCHECKED_CAST")
    private val queryBus = QueryBusImpl(handlers as List<QueryHandler<Query<Any>, Any>>)

    override fun isInstancePerTest() = true

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                queryBus.handlers shouldBe hashMapOf(
                        "com.tgirard12.cqrssimple.stub.AQuery" to handlers[0],
                        "com.tgirard12.cqrssimple.stub.BQuery" to handlers[1],
                        "com.tgirard12.cqrssimple.stub.CQuery" to handlers[2]
                )
            }
            "dispatch Aquery" {
                queryBus.dispatch(AQuery()) shouldBe "AQueryHandler"
            }
            "dispatch Bquery" {
                queryBus.dispatch(BQuery()) shouldBe 3456
            }
            "dispatch Cquery" {
                queryBus.dispatch(CQuery()) shouldBe "BQueryHandler"
            }
            "fail no QueryHandler" {
                shouldThrow<IllegalArgumentException> {
                    queryBus.dispatch(DQuery())
                }
            }
        }
    }
}
