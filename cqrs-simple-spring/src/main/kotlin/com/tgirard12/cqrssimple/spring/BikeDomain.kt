package com.tgirard12.cqrssimple.spring

import com.tgirard12.cqrssimple.*

/** */
class BikeDomain

data class BikeCreateCommand(val name: String) : Command<BikeCreateCommandResponse>
data class BikeCreateCommandResponse(val id: String)

val commandHandler = CommandHandler<BikeCreateCommand, BikeCreateCommandResponse> {
    BikeCreateCommandResponse("id1")
}

data class BikeListQuery(val color: String) : Query<BikeListQueryResponse>
data class BikeListQueryResponse(val bikes: List<String>)

val queryHandler = QueryHandler<BikeListQuery, BikeListQueryResponse> {
    BikeListQueryResponse(listOf("Gravel", "Road"))
}
