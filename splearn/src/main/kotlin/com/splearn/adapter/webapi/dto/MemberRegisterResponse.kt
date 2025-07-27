package com.splearn.adapter.webapi.dto

import com.splearn.domain.member.Member

class MemberRegisterResponse(
    val memberId: Long,
    val email: String
) {
    companion object {
        fun of(member: Member): MemberRegisterResponse {
            return MemberRegisterResponse(
                member.id,
                member.email.address
            )
        }
    }
}


