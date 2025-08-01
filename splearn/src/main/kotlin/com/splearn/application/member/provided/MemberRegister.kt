package com.splearn.application.member.provided

import com.splearn.domain.member.Member
import com.splearn.domain.member.MemberInfoUpdateRequest
import com.splearn.domain.member.MemberRegisterRequest
import jakarta.validation.Valid

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
interface MemberRegister {
    fun register(@Valid registerRequest: MemberRegisterRequest): Member

    fun activate(memberId: Long): Member

    fun deactivate(memberId: Long): Member

    fun updateInfo(memberId: Long, memberInfoUpdateRequest: MemberInfoUpdateRequest): Member
}
