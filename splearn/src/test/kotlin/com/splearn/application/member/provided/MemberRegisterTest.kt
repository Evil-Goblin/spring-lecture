package com.splearn.application.member.provided

import com.splearn.SplearnTestConfiguration
import com.splearn.domain.member.DuplicateEmailException
import com.splearn.domain.member.MemberFixture
import com.splearn.domain.member.MemberRegisterRequest
import com.splearn.domain.member.MemberStatus
import jakarta.validation.ConstraintViolationException
import org.assertj.core.api.Assertions
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@Transactional
@Import(SplearnTestConfiguration::class)
@SpringBootTest
class MemberRegisterTest(
    private val memberRegister: MemberRegister
) {
    @Test
    fun register() {
        val member = memberRegister.register(MemberFixture.createMemberRegisterRequest())

        Assertions.assertThat(member.id).isNotEqualTo(0)
        Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)
        Assertions.assertThat(member.detail.registeredAt).isNotNull
    }

    @Test
    fun duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest())

        Assertions.assertThatThrownBy { memberRegister.register(MemberFixture.createMemberRegisterRequest()) }
            .isInstanceOf(DuplicateEmailException::class.java)
    }

    @Test
    fun activate() {
        val member = memberRegister.register(MemberFixture.createMemberRegisterRequest())
        Assertions.assertThat(member.detail.activatedAt).isNull()

        val activatedMember = memberRegister.activate(member.id)

        Assertions.assertThat(activatedMember.status).isEqualTo(MemberStatus.ACTIVE)
        Assertions.assertThat(activatedMember.detail.activatedAt).isNotNull
    }

    @Test
    fun memberRegisterRequestFail() {
        checkValidation(MemberRegisterRequest("test@test.com", "inva", "invalidCase"))
        checkValidation(MemberRegisterRequest("test@test.com", "invalidinvalidinvalid", "invalidCase"))
        checkValidation(MemberRegisterRequest("test@test.com", "successCase", "invalid"))
        checkValidation(MemberRegisterRequest("test.com", "successCase", "successCase"))
    }

    private fun checkValidation(invalidCase: MemberRegisterRequest) {
        Assertions.assertThatThrownBy { memberRegister.register(invalidCase) }
            .isInstanceOf(ConstraintViolationException::class.java)
    }
}
