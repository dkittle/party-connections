package ca.kittle.capabilities.party.components

import ca.kittle.capabilities.party.models.Party
import ca.kittle.capabilities.pc.components.createCharacterForm
import ca.kittle.capabilities.pc.components.listPlayerCharacters
import ca.kittle.web.TITLE_STYLE
import ca.kittle.web.WIDTH_LARGE
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1

fun FlowContent.populateParty(
    party: Party
) = div {
    partyHeader(party)
    listPlayerCharacters(party.playerCharacters)
    createCharacterForm(party.id)
}

fun FlowContent.partyHeader(party: Party) =
    h1 {
        classes = TITLE_STYLE + WIDTH_LARGE
        +"Party: ${party.name.value}"
    }
