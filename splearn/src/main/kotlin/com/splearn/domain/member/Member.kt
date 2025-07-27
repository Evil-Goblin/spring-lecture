package com.splearn.domain.member

import com.splearn.domain.AbstractEntity
import com.splearn.domain.shared.Email
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.NaturalIdCache

@NaturalIdCache
@Table(name = "MEMBER", uniqueConstraints = [UniqueConstraint(name = "UK_MEMBER_EMAIL_ADDRESS", columnNames = ["email_address"])])
@Entity
class Member
private constructor(
    email: String,
    nickname: String,
    password: String,
    passwordEncoder: PasswordEncoder,
    memberDetail: MemberDetail
): AbstractEntity() {
    companion object {
        fun register(registerRequest: MemberRegisterRequest, passwordEncoder: PasswordEncoder): Member {
            return Member(registerRequest.email, registerRequest.nickname, registerRequest.password, passwordEncoder,
                MemberDetail.create())
        }
    }

    @NaturalId
    @Embedded
    val email: Email = Email(email)

    @Column(length = 100, nullable = false)
    var nickname = nickname
        protected set

    @Column(length = 200, nullable = false)
    var passwordHash = passwordEncoder.encode(password)
        protected set

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    var status = MemberStatus.PENDING
        protected set

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val detail: MemberDetail = memberDetail

    fun activate() {
        if (status != MemberStatus.PENDING) {
            throw IllegalStateException("PENDING 상태가 아닙니다.")
        }
        status = MemberStatus.ACTIVE
        detail.setActivatedAt()
    }

    fun deactivate() {
        if (status != MemberStatus.ACTIVE) {
            throw IllegalStateException("ACTIVE 상태가 아닙니다.")
        }
        status = MemberStatus.DEACTIVATED
        detail.deactivate()
    }

    fun verifyPassword(password: String, encoder: PasswordEncoder): Boolean {
        return encoder.matches(password, passwordHash)
    }

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    fun changePassword(password: String, encoder: PasswordEncoder) {
        this.passwordHash = encoder.encode(password)
    }

    fun isActive(): Boolean {
        return MemberStatus.ACTIVE == status
    }

    fun updateInfo(updateRequest: MemberInfoUpdateRequest) {
        nickname = updateRequest.nickname

        detail.updateInfo(updateRequest)
    }
}
