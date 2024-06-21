package ca.kittle.capabilities.party.components

import ca.kittle.web.BUTTON_STYLE
import ca.kittle.web.FORM_STYLE
import ca.kittle.web.TEXT_INPUT_STYLE
import kotlinx.html.*

fun FlowContent.createPartyForm() =
    form {
        classes = FORM_STYLE
        attributes["hx-post"] = "/party"
        attributes["hx-target"] = "#page-content"
        attributes["hx-ext"] = "json-enc"
        label {
            htmlFor = "name"
            +"Party Name "
        }
        input {
            id = "name"
            name = "name"
            type = InputType.text
            classes = TEXT_INPUT_STYLE
            placeholder = "Enter a party name"
            value = ""
            autoFocus = true
        }
        button {
            classes = BUTTON_STYLE
            type = ButtonType.submit
            +"Create Party"
        }
    }
