package com.splearn.adapter.webapi

import com.splearn.adapter.webapi.dto.MemberRegisterResponse
import com.splearn.application.member.provided.MemberRegister
import com.splearn.domain.member.MemberRegisterRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberApi(
    private val memberRegister: MemberRegister
) {

    @PostMapping("/api/members")
    fun register(@RequestBody @Valid memberRegisterRequest: MemberRegisterRequest): MemberRegisterResponse {
        val member = memberRegister.register(memberRegisterRequest)

        return MemberRegisterResponse.of(member)
    }
}
