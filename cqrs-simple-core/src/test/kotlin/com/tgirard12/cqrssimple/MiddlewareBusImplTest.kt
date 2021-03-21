package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe


class MiddlewareBusImplTest : WordSpec() {

    lateinit var middlewareBus: MiddlewareBusImpl

    override fun beforeEach(testCase: TestCase) {
        middlewareBus = MiddlewareBusImpl().apply {
            register(AMiddlewareHandler())
            register(BMiddlewareHandler)
            register(cMiddlewareHandler)
            register(dMiddlewareHandler)
        }
        MiddlewareCount.reset()
    }

    init {
        "MiddlewareBusImpl" should {
            "Failed if register twice" {
                shouldThrow<IllegalArgumentException> {
                    middlewareBus.register(cMiddlewareHandler2)
                }
            }
            "handlers map" {
                middlewareBus.preHandlers.keys shouldBe listOf(AMiddleware::class, CMiddleware::class)
                middlewareBus.postHandlers.keys shouldBe listOf(BMiddleware::class, DMiddleware::class)
            }
        }
    }
}
