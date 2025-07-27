package com.splearn.domain.member

import com.splearn.domain.member.MemberFixture.Companion.createMemberRegisterRequest
import com.splearn.domain.member.MemberFixture.Companion.createPasswordEncoder
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class MemberTest {

    lateinit var encoder: PasswordEncoder

    lateinit var member: Member

    @BeforeEach
    fun setUp() {
        encoder = createPasswordEncoder()
        member = Member.register(createMemberRegisterRequest(), encoder)
    }

    @Test
    fun createMember() {
        assertThat(member.status).isEqualTo(MemberStatus.PENDING)
    }

    @Test
    fun activate() {
        assertThat(member.detail.activatedAt).isNull()

        member.activate()

        assertThat(member.status).isEqualTo(MemberStatus.ACTIVE)
        assertThat(member.detail.activatedAt).isNotNull
    }

    @Test
    fun activateFail() {
        member.activate()

        assertThatThrownBy { member.activate() }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun deactivate() {
        member.activate()
        member.deactivate()

        assertThat(member.status).isEqualTo(MemberStatus.DEACTIVATED)
        assertThat(member.detail.deactivatedAt).isNotNull
    }

    @Test
    fun deactivateFail() {
        assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)

        member.activate()
        member.deactivate()

        assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun verifyPassword() {
        assertThat(member.verifyPassword("testPassword", encoder))
            .isTrue
    }

    @Test
    fun changeNickname() {
        assertThat(member.nickname).isEqualTo("testNickname")

        member.changeNickname("nickname")

        assertThat(member.nickname).isEqualTo("nickname")
    }

    @Test
    fun changePassword() {
        member.changePassword("password", encoder)

        assertThat(member.verifyPassword("password", encoder)).isTrue
    }

    @Test
    fun isActive() {
        assertThat(member.isActive()).isFalse

        member.activate()

        assertThat(member.isActive()).isTrue

        member.deactivate()

        assertThat(member.isActive()).isFalse
    }

    @Test
    fun invalidEmail() {
        assertThatThrownBy { Member.register(MemberRegisterRequest("test", "test", "test"), encoder) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun equals() {
        val register = Member.register(createMemberRegisterRequest(), encoder)
        val sameEmailMember = Member.register(createMemberRegisterRequest("notEquals@mail.com"), encoder)

        assertThat(register.id).isEqualTo(sameEmailMember.id)
        assertThat(register).isEqualTo(sameEmailMember)
        assertThat(register.hashCode()).isEqualTo(sameEmailMember.hashCode())
    }

    @Test
    fun updateInfo() {
        member.activate()

        member.updateInfo(MemberInfoUpdateRequest("changeNickname", "toby100", "자기소개"))

        Assertions.assertThat(member.nickname).isEqualTo("changeNickname")
        assertThat(member.detail.profile?.address).isEqualTo("toby100")
        Assertions.assertThat(member.detail.introduction).isEqualTo("자기소개")
    }
}
