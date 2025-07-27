package com.splearn.adapter.webapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.splearn.AssertThatUtils.Companion.equalTo
import com.splearn.AssertThatUtils.Companion.notNull
import com.splearn.adapter.webapi.dto.MemberRegisterResponse
import com.splearn.application.member.provided.MemberRegister
import com.splearn.application.member.required.MemberRepository
import com.splearn.domain.member.MemberFixture
import com.splearn.domain.member.MemberStatus
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberApiTest(
    val mvcTester: MockMvcTester,
    val objectMapper: ObjectMapper,
    val memberRepository: MemberRepository,
    val memberRegister: MemberRegister
) {

    @Test
    fun register() {
        val request = MemberFixture.createMemberRegisterRequest()
        val requestJson = objectMapper.writeValueAsString(request)

        val result = mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson).exchange()

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.memberId", notNull())
            .hasPathSatisfying("$.email", equalTo(request.email))

        val response = result.response.contentAsString.let {
            objectMapper.readValue(it, MemberRegisterResponse::class.java)
        }

        val memberOptional = memberRepository.findById(response.memberId)
        assertThat(memberOptional).isNotNull
        val member = memberOptional!!

        assertThat(member.nickname).isEqualTo(request.nickname)
        assertThat(member.email.address).isEqualTo(request.email)
        assertThat(member.status).isEqualTo(MemberStatus.PENDING)
    }

    @Test
    fun duplicateEmail() {
        val request = MemberFixture.createMemberRegisterRequest()
        memberRegister.register(request)

        val requestJson = objectMapper.writeValueAsString(request)

        val result = mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson).exchange()

        assertThat(result)
            .apply(MockMvcResultHandlers.print())
            .hasStatus(HttpStatus.CONFLICT)
    }

}
