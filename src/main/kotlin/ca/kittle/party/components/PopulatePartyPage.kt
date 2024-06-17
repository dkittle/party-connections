package ca.kittle.party.components

import ca.kittle.party.models.Party
import ca.kittle.party.models.PlayerCharacter
import ca.kittle.web.*
import kotlinx.html.*

fun FlowContent.populateParty(
    party: Party,
    pcs: List<PlayerCharacter>,
) = div {
    partyHeader(party)
    listPartyCharacters(pcs)
    addCharactersForm(party.id)
}

fun FlowContent.partyHeader(party: Party) =
    h1 {
        classes = TITLE_STYLE + WIDTH_LARGE
        +"Party: ${party.name}"
    }

fun FlowContent.listPartyCharacters(pcs: List<PlayerCharacter>) =
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

fun FlowContent.addCharactersForm(partyId: String) =
    form {
        classes = FORM_STYLE + WIDTH_LARGE
        attributes["hx-post"] = "/character"
        attributes["hx-target"] = "#page-content"
        attributes["hx-ext"] = "json-enc"
        input {
            type = InputType.hidden
            name = "partyId"
            value = partyId
        }
        div {
            classes = FOUR_COLUMNS
            div {
                classes = ONE_COLUMN_SPAN
                label {
                    classes = LABEL_STYLE
                    htmlFor = "name"
                    +"PC Name"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                input {
                    id = "name"
                    name = "name"
                    type = InputType.text
                    classes = TEXT_INPUT_STYLE
                    value = ""
                    required = true
                    autoFocus = true
                }
            }
        }
        div {
            classes = FOUR_COLUMNS
            div {
                classes = ONE_COLUMN_SPAN
                label {
                    classes = LABEL_STYLE
                    htmlFor = "gender"
                    +"Gender"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                input {
                    id = "gender"
                    name = "gender"
                    type = InputType.text
                    required = true
                    classes = TEXT_INPUT_STYLE
                    value = ""
                }
            }
        }
        div {
            classes = FOUR_COLUMNS
            div {
                classes = ONE_COLUMN_SPAN
                label {
                    classes = LABEL_STYLE
                    htmlFor = "ancestry"
                    +"Ancestry"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                input {
                    id = "ancestry"
                    name = "ancestry"
                    type = InputType.text
                    required = true
                    classes = TEXT_INPUT_STYLE
                    value = ""
                }
            }
        }
        div {
            classes = FOUR_COLUMNS

            div {
                classes = ONE_COLUMN_SPAN
                label {
                    classes = LABEL_STYLE
                    htmlFor = "baseClass"
                    +"Base Class"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                input {
                    id = "baseClass"
                    name = "baseClass"
                    type = InputType.text
                    required = true
                    classes = TEXT_INPUT_STYLE
                    value = ""
                }
            }
        }
        div {
            classes = FOUR_COLUMNS
            div {
                classes = ONE_COLUMN_SPAN
                label {
                    classes = LABEL_STYLE
                    htmlFor = "role"
                    +"Party Role"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                input {
                    id = "role"
                    name = "role"
                    type = InputType.text
                    required = true
                    classes = TEXT_INPUT_STYLE
                    value = ""
                }
            }
        }
        div {
            classes = FOUR_COLUMNS
            div {
                classes = ONE_COLUMN_SPAN
                label {
                    classes = LABEL_STYLE
                    htmlFor = "backstory"
                    +"Backstory"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                textArea {
                    id = "backstory"
                    name = "backstory"
                    required = true
                    classes = TEXT_INPUT_STYLE
                    minLength = "1"
                    maxLength = "5000"
                    rows = "5"
                    cols = "40"
                }
            }
        }
        button {
            classes = BUTTON_STYLE
            type = ButtonType.submit
            +"Create Character"
        }
    }
