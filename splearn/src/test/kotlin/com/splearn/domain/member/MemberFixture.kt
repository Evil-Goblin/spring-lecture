package com.splearn.domain.member

import org.springframework.test.util.ReflectionTestUtils

class MemberFixture {
    companion object {
        fun createPasswordEncoder(): PasswordEncoder = object : PasswordEncoder {
            override fun encode(password: String): String {
                return password
            }

            override fun matches(password: String, passwordHash: String): Boolean {
                return passwordHash == password
            }
        }

        fun createMemberRegisterRequest(email: String = "test@test.com"): MemberRegisterRequest =
            MemberRegisterRequest(email, "testNickname", "testPassword")

        fun createMember(): Member {
            return Member.register(createMemberRegisterRequest(), createPasswordEncoder())
        }

        fun createMember(id: Long): Member {
            val member = Member.register(createMemberRegisterRequest(), createPasswordEncoder())
            ReflectionTestUtils.setField(member, "id", id)
            return member
        }

        fun createMember(email: String): Member {
            return Member.register(createMemberRegisterRequest(email), createPasswordEncoder())
        }
    }
}
