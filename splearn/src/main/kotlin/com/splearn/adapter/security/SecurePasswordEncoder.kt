package com.splearn.adapter.security

import com.splearn.domain.member.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurePasswordEncoder: PasswordEncoder {

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }

    override fun matches(password: String, passwordHash: String): Boolean {
        return passwordEncoder.matches(password, passwordHash)
    }
}
