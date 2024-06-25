package ca.kittle.capabilities.pc.components

import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.web.ITEM_GRID_STYLE
import ca.kittle.web.ROUNDED_BOX
import ca.kittle.web.UNORDERED_LIST_STYLE
import kotlinx.html.*

fun FlowContent.listPlayerCharacters(pcs: List<PlayerCharacter>) =
    div {
        classes = ROUNDED_BOX
        ul {
            id = "party-list"
            classes = UNORDERED_LIST_STYLE
            pcs.map { pc ->
                li {
                    classes = ITEM_GRID_STYLE
                    div {
                        classes = setOf("font-bold")
                        +pc.name
                    }
                    div {
                        +pc.ancestry
                    }
                    div {
                        +pc.baseClass
                    }
                    div {
                        +pc.role
                    }
                }
            }
        }
    }
