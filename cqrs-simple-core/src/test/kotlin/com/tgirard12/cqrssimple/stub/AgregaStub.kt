package com.tgirard12.cqrssimple.stub

import com.tgirard12.cqrssimple.Agrega
import com.tgirard12.cqrssimple.AgregaHandler
import com.tgirard12.cqrssimple.Event

/**
 * Agrega
 */
data class AAgrega(
    val name: String,
    val email: String
) : Agrega

/**
 * Events
 */
sealed class AAgregaEvent : Event {
    data class UpdateNameEvent(val newName: String) : AAgregaEvent()
    data class UpdateEmailEvent(val newEmail: String) : AAgregaEvent()
}


/**
 * Handler
 */
class AAgregaHandler : AgregaHandler<AAgrega, AAgregaEvent> {

    override fun neutral() = AAgrega("", "")

    override fun AAgrega.fold(event: AAgregaEvent) = when (event) {
        is AAgregaEvent.UpdateNameEvent -> this.copy(name = event.newName)
        is AAgregaEvent.UpdateEmailEvent -> this.copy(email = event.newEmail)
    }
}