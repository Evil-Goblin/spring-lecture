package com.splearn.application.member.required

import com.splearn.domain.member.Member
import com.splearn.domain.shared.Email
import org.springframework.data.repository.Repository

/**
 * 회원 정보를 저장하거나 조회한다.
 */
interface MemberRepository: Repository<Member, Long> {
    fun save(member: Member): Member
    fun findById(memberId: Long): Member?
    fun findByEmail(email: Email): Member?
}
