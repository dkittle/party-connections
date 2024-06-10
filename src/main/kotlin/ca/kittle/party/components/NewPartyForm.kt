package ca.kittle.party.components

import kotlinx.html.*

private fun FlowContent.newPartyForm(
    nameEnabled: Boolean,
    emailEnabled: Boolean,
    lastOnlineEnabled: Boolean,
) = form {
    classes = setOf("max-w-sm", "mx-auto", "my-4", "gap-2")

    input {
        id = "name"
        name = "name"
        type = InputType.text
        classes = setOf("w-full", "h-8", "border-2", "border-neutral-200", "rounded-md")
        value = ""
        autoFocus = true
    }
    label {
        htmlFor = "name"
        +" Name"
    }
    br { }
    button {

    }
}
