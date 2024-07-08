package ca.kittle.capabilities.party.components

import ca.kittle.capabilities.party.models.PartyId
import ca.kittle.capabilities.party.models.PartyMember
import ca.kittle.web.ITEM_GRID_STYLE
import ca.kittle.web.ROUNDED_BOX
import ca.kittle.web.UNORDERED_LIST_STYLE
import kotlinx.html.*


fun FlowContent.listPartyMembers(partyId: PartyId, pcs: List<PartyMember>) =
    div {
        classes = ROUNDED_BOX
        id = "party-list"
        attributes["hx-get"] = "/party/${partyId.value}/members"
        attributes["hx-trigger"] = "load delay:10s"
        attributes["hx-swap"] = "outerHTML"
        ul {
            classes = UNORDERED_LIST_STYLE
            pcs.map { pc ->
                val summary = pc.backstorySummary?.let {
                    "✅summary"
                } ?: "🟠summarizing "

                li {
                    classes = ITEM_GRID_STYLE
                    div {
                        classes = setOf("font-bold")
                        +pc.name.value
                    }
                    div {
                        +pc.ancestry.value
                    }
                    div {
                        +pc.baseClass.value
                    }
                    div {
                        +pc.role.value
                    }
                    div {
                        +summary
                    }
                }
            }
        }
    }
