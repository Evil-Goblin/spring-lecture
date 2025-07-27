package com.splearn.adapter.integration

import com.splearn.domain.shared.Email
import org.assertj.core.api.Assertions
import org.junitpioneer.jupiter.StdIo
import org.junitpioneer.jupiter.StdOut
import kotlin.test.Test

class DummyEmailSenderTest {

    @StdIo
    @Test
    fun dummyEmailSender(out: StdOut) {
        val dummyEmailSender = DummyEmailSender()

        dummyEmailSender.send(Email("test@test.com"), "test", "test")

        Assertions.assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender: Email(address='test@test.com')")
    }

}
