package ca.kittle.party.components

import ca.kittle.party.models.Party
import kotlinx.html.FlowContent
import kotlinx.html.li
import kotlinx.html.ul

fun FlowContent.listOfParties(parties: List<Party>) =
    ul {
        for (party in parties) {
            li {
                attributes["hx-get"] = "/party/${party.id}"
                attributes["hx-target"] = "#page-content"
                +party.name
            }
        }
    }
