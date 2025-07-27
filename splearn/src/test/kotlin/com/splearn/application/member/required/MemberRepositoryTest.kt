package com.splearn.application.member.required

import com.splearn.domain.member.Member
import com.splearn.domain.member.MemberFixture.Companion.createMemberRegisterRequest
import com.splearn.domain.member.MemberFixture.Companion.createPasswordEncoder
import com.splearn.domain.member.MemberStatus
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import kotlin.test.Test

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createMember() {
        val member = Member.register(
            createMemberRegisterRequest(),
            createPasswordEncoder()
        )

        assertThat(member.id).isEqualTo(0)

        memberRepository.save(member)

        assertThat(member.id).isNotEqualTo(0)

        entityManager.flush()
        entityManager.clear()

        val found = memberRepository.findById(member.id)
        assertThat(found).isNotNull
        assertThat(found?.status).isEqualTo(MemberStatus.PENDING)
        assertThat(found?.detail?.registeredAt).isNotNull
    }

    @Test
    fun duplicateEmailFail() {
        val member = Member.register(
            createMemberRegisterRequest(),
            createPasswordEncoder()
        )
        memberRepository.save(member)

        val sameEmailMember = Member.register(
            createMemberRegisterRequest(),
            createPasswordEncoder()
        )
        assertThatThrownBy { memberRepository.save(sameEmailMember) }
            .isInstanceOf(DataIntegrityViolationException::class.java)
    }
}
