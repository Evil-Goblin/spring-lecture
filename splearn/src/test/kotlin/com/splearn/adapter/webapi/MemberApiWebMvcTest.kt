package com.splearn.adapter.webapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.splearn.application.member.provided.MemberRegister
import com.splearn.domain.member.MemberFixture
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.assertj.MockMvcTester
import kotlin.test.Test

@WebMvcTest(MemberApi::class)
class MemberApiWebMvcTest(
    val mockMvcTester: MockMvcTester,
    @MockitoBean
    val memberRegister: MemberRegister,
    val objectMapper: ObjectMapper
) {

    @Test
    fun register() {
        val member = MemberFixture.createMember(1)
        Mockito.`when`(memberRegister.register(any())).thenReturn(member)

        val request = MemberFixture.createMemberRegisterRequest()
        val requestJson = objectMapper.writeValueAsString(request)

        Assertions.assertThat(mockMvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .hasStatusOk()
            .bodyJson()
            .extractingPath("$.memberId").asNumber().isEqualTo(1)

        verify(memberRegister).register(request)
    }

    @Test
    fun registerFail() {
        val request = MemberFixture.createMemberRegisterRequest("invalidEmail")
        val requestJson = objectMapper.writeValueAsString(request)

        Assertions.assertThat(mockMvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .hasStatus(HttpStatus.BAD_REQUEST)
    }
}
