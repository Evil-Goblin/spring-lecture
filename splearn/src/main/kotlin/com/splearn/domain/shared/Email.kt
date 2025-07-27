package com.splearn.domain.shared

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.regex.Pattern

@Embeddable
class Email(
    @Column(name = "email_address", length = 150, nullable = false)
    val address: String
) {
    companion object {
        private val pattern =
            Pattern.compile("^[a-zA-Z0-9_=&*-]+(?:\\.[a-zA-Z0-9_=&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    }

    init {
        require(pattern.matcher(address).matches()) {
            "이메일 형식이 바르지 않습니다: $address"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Email

        return address == other.address
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }

    override fun toString(): String {
        return "Email(address='$address')"
    }


}
