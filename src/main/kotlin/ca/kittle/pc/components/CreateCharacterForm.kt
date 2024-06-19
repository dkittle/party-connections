package ca.kittle.pc.components

import ca.kittle.web.*
import kotlinx.html.*

fun FlowContent.createCharacterForm(partyId: String) =
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
                    placeholder = "Character name"
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
                    placeholder = "Elf, Dwarf, Human, etc"
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
                    placeholder = "Cleric, Fighter, Wizard, etc"
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
                    htmlFor = "background"
                    +"Background"
                }
            }
            div {
                classes = THREE_COLUMN_SPAN
                input {
                    id = "background"
                    name = "background"
                    type = InputType.text
                    placeholder = "Acolyte, Sage, Urchin, etc"
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
                    placeholder = "Controller, Tank, The Face, etc"
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
                    cols = ""
                }
            }
        }
        button {
            classes = BUTTON_STYLE
            type = ButtonType.submit
            +"Create Character"
        }
    }
