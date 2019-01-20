package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.Description
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec


class MiddlewareBusImplTest : WordSpec() {

    private val handlers = listOf(
            AMiddlewareHandler(),
            BMiddlewareHandler(),
            cMiddlewareHandler,
            cMiddlewareHandler2
    )

    @Suppress("UNCHECKED_CAST")
    private val middlewareBus = MiddlewareBusImpl(handlers as List<MiddlewareHandler<Middleware>>)

    override fun isInstancePerTest(): Boolean = true

    override fun beforeTest(description: Description) {
        MiddlewareCount.reset()
    }

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                middlewareBus.handlers shouldBe hashMapOf(
                        "com.tgirard12.cqrssimple.stub.AMiddleware" to listOf(handlers[0]),
                        "com.tgirard12.cqrssimple.stub.BMiddleware" to listOf(handlers[1]),
                        "com.tgirard12.cqrssimple.stub.CMiddleware" to listOf(handlers[2], handlers[3])
                )
            }
            "dispatch AMiddleware" {
                middlewareBus.dispatch(AMiddleware())
                MiddlewareCount.aCount shouldBe "a1"
                MiddlewareCount.bCount shouldBe "b"
                MiddlewareCount.cCount shouldBe "c"
                MiddlewareCount.dCount shouldBe "d"
            }
            "dispatch BMiddleware" {
                middlewareBus.dispatch(BMiddleware())
                MiddlewareCount.aCount shouldBe "a"
                MiddlewareCount.bCount shouldBe "b2"
                MiddlewareCount.cCount shouldBe "c"
                MiddlewareCount.dCount shouldBe "d"
            }
            "dispatch CMiddleware-> 2 events" {
                middlewareBus.dispatch(CMiddleware())
                MiddlewareCount.aCount shouldBe "a"
                MiddlewareCount.bCount shouldBe "b"
                MiddlewareCount.cCount shouldBe "c34"
                MiddlewareCount.dCount shouldBe "d"
            }
            "fail no MiddlewareHandler" {
                middlewareBus.dispatch(DMiddleware())
                MiddlewareCount.aCount shouldBe "a"
                MiddlewareCount.bCount shouldBe "b"
                MiddlewareCount.cCount shouldBe "c"
                MiddlewareCount.dCount shouldBe "d"
            }
        }
    }
}
