package com.splearn.domain.member

import org.assertj.core.api.Assertions
import kotlin.test.Test

class ProfileTest {

    @Test
    fun profile() {
        Profile("tobyilee")
        Profile("toby100")
        Profile("12345")
        Profile("testaddress")
    }

    @Test
    fun profileFail() {
        Assertions.assertThatThrownBy { Profile("1234512345123451") }
            .isInstanceOf(IllegalArgumentException::class.java)
        Assertions.assertThatThrownBy { Profile("") }
            .isInstanceOf(IllegalArgumentException::class.java)
        Assertions.assertThatThrownBy { Profile("ABCD1") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun url() {
        val profile = Profile("tobyilee")

        Assertions.assertThat(profile.url()).isEqualTo("@tobyilee")
    }
}
