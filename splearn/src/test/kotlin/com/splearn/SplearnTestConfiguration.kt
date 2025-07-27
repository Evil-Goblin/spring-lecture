package com.splearn

import com.splearn.application.member.required.EmailSender
import com.splearn.domain.member.MemberFixture
import com.splearn.domain.member.PasswordEncoder
import com.splearn.domain.shared.Email
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class SplearnTestConfiguration {
    @Bean
    fun emailSender(): EmailSender {
        return object : EmailSender {
            override fun send(email: Email, subject: String, content: String) {
                println("Sending email: $email")
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return MemberFixture.Companion.createPasswordEncoder()
    }
}
