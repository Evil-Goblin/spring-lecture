package com.splearn.application.member.required

import com.splearn.domain.shared.Email

/**
 * 이메일을 발송한다.
 */
interface EmailSender {
    fun send(email: Email, subject: String, content: String)
}
