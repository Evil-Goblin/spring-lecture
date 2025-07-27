package com.splearn.adapter.security

import org.assertj.core.api.Assertions
import kotlin.test.Test

class SecurePasswordEncoderTest {
    @Test
    fun securePasswordEncoder() {
        val securePasswordEncoder = SecurePasswordEncoder()
        
        val passwordHash = securePasswordEncoder.encode("test")

        Assertions.assertThat(securePasswordEncoder.matches("test", passwordHash)).isTrue
        Assertions.assertThat(securePasswordEncoder.matches("invalid", passwordHash)).isFalse
    }

}
