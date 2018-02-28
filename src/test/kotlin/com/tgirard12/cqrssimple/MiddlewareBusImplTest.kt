package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotlintest.TestCaseContext
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.WordSpec


@Suppress("UNCHECKED_CAST")
class MiddlewareBusImplTest : WordSpec() {

    private val handlers = listOf(
            AMiddlewareHandler(),
            BMiddlewareHandler(),
            cMiddlewareHandler,
            cMiddlewareHandler2
    )

    private val middlewareBus = MiddlewareBusImpl(handlers as List<MiddlewareHandler<Middleware>>)

    override val oneInstancePerTest: Boolean = true

    override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        MiddlewareCount.reset()
        test()
    }

    init {
        "QueryBusImplTest" should {
            "handlers map" {
                middlewareBus.handlers shouldEqual hashMapOf(
                        "com.tgirard12.cqrssimple.stub.AMiddleware" to listOf(handlers[0]),
                        "com.tgirard12.cqrssimple.stub.BMiddleware" to listOf(handlers[1]),
                        "com.tgirard12.cqrssimple.stub.CMiddleware" to listOf(handlers[2], handlers[3])
                )
            }
            "dispatch AMiddleware" {
                middlewareBus.dispatch(AMiddleware())
                MiddlewareCount.aCount shouldEqual "a1"
                MiddlewareCount.bCount shouldEqual "b"
                MiddlewareCount.cCount shouldEqual "c"
                MiddlewareCount.dCount shouldEqual "d"
            }
            "dispatch BMiddleware" {
                middlewareBus.dispatch(BMiddleware())
                MiddlewareCount.aCount shouldEqual "a"
                MiddlewareCount.bCount shouldEqual "b2"
                MiddlewareCount.cCount shouldEqual "c"
                MiddlewareCount.dCount shouldEqual "d"
            }
            "dispatch CMiddleware-> 2 events" {
                middlewareBus.dispatch(CMiddleware())
                MiddlewareCount.aCount shouldEqual "a"
                MiddlewareCount.bCount shouldEqual "b"
                MiddlewareCount.cCount shouldEqual "c34"
                MiddlewareCount.dCount shouldEqual "d"
            }
            "fail no MiddlewareHandler" {
                middlewareBus.dispatch(DMiddleware())
                MiddlewareCount.aCount shouldEqual "a"
                MiddlewareCount.bCount shouldEqual "b"
                MiddlewareCount.cCount shouldEqual "c"
                MiddlewareCount.dCount shouldEqual "d"
            }
        }
    }
}
