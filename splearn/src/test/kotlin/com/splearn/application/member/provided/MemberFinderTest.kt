package com.splearn.application.member.provided

import com.splearn.SplearnTestConfiguration
import com.splearn.domain.member.MemberFixture
import org.assertj.core.api.Assertions
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@Transactional
@Import(SplearnTestConfiguration::class)
@SpringBootTest
class MemberFinderTest(
    private val memberFinder: MemberFinder,
    private val memberRegister: MemberRegister
) {

    @Test
    fun find() {
        val member = memberRegister.register(MemberFixture.createMemberRegisterRequest())

        val found = memberFinder.find(member.id)

        Assertions.assertThat(member).isEqualTo(found)
        Assertions.assertThat(member.id).isEqualTo(found.id)
        Assertions.assertThat(member.nickname).isEqualTo(found.nickname)
        Assertions.assertThat(member.email).isEqualTo(found.email)
    }

    @Test
    fun findFail() {
        Assertions.assertThatThrownBy { memberFinder.find(Long.MAX_VALUE) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
