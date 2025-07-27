package com.splearn.application.member.provided

import com.splearn.application.member.MemberModifyService
import com.splearn.application.member.required.EmailSender
import com.splearn.application.member.required.MemberRepository
import com.splearn.domain.member.Member
import com.splearn.domain.member.MemberFixture
import com.splearn.domain.member.MemberStatus
import com.splearn.domain.shared.Email
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.Test

class MemberRegisterManualTest {

    @Test
    fun registerTestStub() {
        val memberRegister = MemberModifyService(
            memberRepository = MemberRepositoryStub(),
            emailSender = EmailSenderStub(),
            passwordEncoder = MemberFixture.createPasswordEncoder(),
            memberFinder = MemberFinderStub()
        )

        val member = memberRegister.register(MemberFixture.createMemberRegisterRequest())

        Assertions.assertThat(member.id).isNotEqualTo(0)
        Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)
    }

    @Test
    fun registerTestMock() {
        val emailSender = EmailSenderMock()
        val memberRegister = MemberModifyService(
            memberRepository = MemberRepositoryStub(),
            emailSender = emailSender,
            passwordEncoder = MemberFixture.createPasswordEncoder(),
            memberFinder = MemberFinderStub()
        )

        val member = memberRegister.register(MemberFixture.createMemberRegisterRequest())

        Assertions.assertThat(member.id).isNotEqualTo(0)
        Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)
        Assertions.assertThat(emailSender.emails).hasSize(1)
        Assertions.assertThat(emailSender.emails.first()).isEqualTo(member.email)
    }

    @Test
    fun registerTestMockito() {
        val emailSender = Mockito.mock(EmailSender::class.java)

        val memberRegister = MemberModifyService(
            memberRepository = MemberRepositoryStub(),
            emailSender = emailSender,
            passwordEncoder = MemberFixture.createPasswordEncoder(),
            memberFinder = MemberFinderStub()
        )

        val member = memberRegister.register(MemberFixture.createMemberRegisterRequest())

        Assertions.assertThat(member.id).isNotEqualTo(0)
        Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)

        Mockito.verify(emailSender).send(eq(member.email), any(), any())
    }

    companion object {
        class MemberRepositoryStub: MemberRepository {
            override fun save(member: Member): Member {
                ReflectionTestUtils.setField(member, "id", 1)
                return member
            }

            override fun findById(memberId: Long): Member? {
                return null
            }

            override fun findByEmail(email: Email): Member? {
                return null
            }
        }

        class EmailSenderStub: EmailSender {
            override fun send(email: Email, subject: String, content: String) {
            }
        }

        class EmailSenderMock: EmailSender {
            val emails = mutableListOf<Email>()

            override fun send(email: Email, subject: String, content: String) {
                emails.add(email)
            }
        }

        class MemberFinderStub: MemberFinder {
            override fun find(memberId: Long): Member {
                return MemberFixture.createMember(memberId)
            }

        }
    }
}
