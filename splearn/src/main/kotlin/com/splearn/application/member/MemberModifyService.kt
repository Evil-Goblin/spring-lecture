package com.splearn.application.member

import com.splearn.application.member.provided.MemberFinder
import com.splearn.application.member.provided.MemberRegister
import com.splearn.application.member.required.EmailSender
import com.splearn.application.member.required.MemberRepository
import com.splearn.domain.member.*
import com.splearn.domain.shared.Email
import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Validated
@Transactional
@Service
class MemberModifyService(
    private val memberRepository: MemberRepository,
    private val memberFinder: MemberFinder,
    private val emailSender: EmailSender,
    private val passwordEncoder: PasswordEncoder
): MemberRegister {

    override fun register(@Valid registerRequest: MemberRegisterRequest): Member {
        // check
        validateDuplicateEmail(registerRequest)

        // domain model
        val member = Member.register(registerRequest, passwordEncoder)

        // repository
        memberRepository.save(member)

        // post process
        sendWelcomeEmail(member)

        return member
    }

    override fun activate(memberId: Long): Member {
        val member = memberFinder.find(memberId)

        member.activate()

        return memberRepository.save(member)
    }

    override fun deactivate(memberId: Long): Member {
        val member = memberFinder.find(memberId)

        member.deactivate()

        return memberRepository.save(member)
    }

    override fun updateInfo(
        memberId: Long,
        memberInfoUpdateRequest: MemberInfoUpdateRequest
    ): Member {
        val member = memberFinder.find(memberId)

        member.updateInfo(memberInfoUpdateRequest)

        return memberRepository.save(member)
    }

    private fun sendWelcomeEmail(member: Member) {
        emailSender.send(member.email, "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요")
    }

    private fun validateDuplicateEmail(registerRequest: MemberRegisterRequest) {
        if (memberRepository.findByEmail(Email(registerRequest.email)) != null) {
            throw DuplicateEmailException("이미 사용중인 이메일입니다: ${registerRequest.email}")
        }
    }
}
