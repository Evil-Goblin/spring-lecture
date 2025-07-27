package com.splearn.domain.shared

import org.assertj.core.api.Assertions
import kotlin.test.Test

class EmailTest {
    @Test
    fun equality() {
        val firstEmail = Email("test@test.com")
        val secondEmail = Email("test@test.com")

        Assertions.assertThat(firstEmail).isEqualTo(secondEmail)
    }
}
