package com.tgirard12.cqrssimple

import com.tgirard12.cqrssimple.stub.AAgrega
import com.tgirard12.cqrssimple.stub.AAgregaEvent.UpdateEmailEvent
import com.tgirard12.cqrssimple.stub.AAgregaEvent.UpdateNameEvent
import com.tgirard12.cqrssimple.stub.AAgregaHandler
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

/**
 *
 */
internal class AgregaTest : WordSpec() {

    init {
        "AgregaHandlertest" should {
            "create neutral Agrega" {
                AAgregaHandler().neutral() shouldBe AAgrega("", "")
            }
            "fold UpdateNameEvent event" {
                AAgregaHandler().fold(
                    listOf(UpdateNameEvent("Foo"))
                ) shouldBe AAgrega("Foo", "")
            }
            "fold UpdateEmailEvent event" {
                AAgregaHandler().fold(
                    listOf(UpdateEmailEvent("foo@bar.com"))
                ) shouldBe AAgrega("", "foo@bar.com")
            }
            "fold two events" {
                AAgregaHandler().fold(
                    listOf(UpdateNameEvent("Foo"), UpdateEmailEvent("foo@bar.com"))
                ) shouldBe AAgrega("Foo", "foo@bar.com")
            }
        }
    }
}