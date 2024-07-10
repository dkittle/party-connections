package ca.kittle

import ca.kittle.capabilities.pc.models.Name
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class ValueClassTest {


    @Test
    fun `test Name value class`() {
        assertFails { Name("") }
        assertFails { Name("   ") }
        assertTrue { Name("Don").value == "Don" }
    }
}

