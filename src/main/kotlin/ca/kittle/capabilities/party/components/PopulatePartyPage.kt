package ca.kittle.capabilities.party.components

import ca.kittle.capabilities.party.models.Party
import ca.kittle.capabilities.pc.components.createCharacterForm
import ca.kittle.capabilities.pc.components.listPlayerCharacters
import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.web.*
import kotlinx.html.*

fun FlowContent.populateParty(
    party: Party,
    pcs: List<PlayerCharacter>,
) = div {
    partyHeader(party)
    listPlayerCharacters(pcs)
    createCharacterForm(party.id)
}

fun FlowContent.partyHeader(party: Party) =
    h1 {
        classes = TITLE_STYLE + WIDTH_LARGE
        +"Party: ${party.name}"
    }
