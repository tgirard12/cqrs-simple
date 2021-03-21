package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe


class QueryBusImplTest : WordSpec() {

    private lateinit var queryBus: QueryBusImpl

    override fun beforeEach(testCase: TestCase) {
        queryBus = QueryBusImpl().apply {
            register(AQueryHandler())
            register(BQueryHandler)
            register(cQueryHandler)
        }
    }

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                queryBus.handlers.keys shouldBe listOf(
                    "com.tgirard12.cqrssimple.stub.AQuery",
                    "com.tgirard12.cqrssimple.stub.BQuery",
                    "com.tgirard12.cqrssimple.stub.CQuery",
                )
            }
            "failed if registed twice" {
                shouldThrow<IllegalArgumentException> { queryBus.register(cQueryHandler2) }
            }
            "dispatch Aquery" {
                queryBus.dispatch(AQuery()) shouldBe "AQueryHandler"
            }
            "dispatch Bquery" {
                queryBus.dispatch(BQuery()) shouldBe 3456
            }
            "dispatch Cquery" {
                queryBus.dispatch(CQuery()) shouldBe "CQueryHandler"
            }
            "fail no QueryHandler" {
                shouldThrow<IllegalArgumentException> {
                    queryBus.dispatch(DQuery())
                }
            }
        }
    }
}
