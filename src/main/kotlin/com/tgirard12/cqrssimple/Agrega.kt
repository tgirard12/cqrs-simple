package com.tgirard12.cqrssimple

/**
 * Event sourcing Agrega
 */
interface Agrega

/**
 * Handler to fold events E on Agrega A
 */
interface AgregaHandler<A : Agrega, E : Event> {

    fun neutral(): A

    fun A.fold(event: E): A

    fun fold(events: List<E>): A =
        events.fold(neutral()) { agrega, event ->
            agrega.fold(event)
        }
}
