package com.splearn.adapter.integration

import com.splearn.application.member.required.EmailSender
import com.splearn.domain.shared.Email
import org.springframework.context.annotation.Fallback
import org.springframework.stereotype.Component

@Fallback
@Component
class DummyEmailSender: EmailSender {
    override fun send(email: Email, subject: String, content: String) {
        println("DummyEmailSender: $email")
    }
}
