package com.splearn.domain.member

import jakarta.persistence.Embeddable
import java.util.regex.Pattern

@Embeddable
class Profile(
    val address: String
) {
    companion object {
        private val pattern =
            Pattern.compile("[a-z0-9]+")
    }

    init {
        require(pattern.matcher(address).matches()) {
            "프로필 주소 형식이 바르지 않습니다: $address"
        }

        require(address.length <= 15) {
            "프로필 주소는 최대 15자리를 넘을 수 없습니다."
        }
    }

    fun url(): String {
        return "@$address"
    }
}
