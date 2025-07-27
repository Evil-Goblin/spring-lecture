package com.splearn.domain.member

import com.splearn.domain.AbstractEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class MemberDetail : AbstractEntity() {
    internal companion object {
        fun create(): MemberDetail {
            return MemberDetail()
        }
    }

    @Embedded
    var profile: Profile? = null
        protected set
    var introduction: String = ""
        protected set
    val registeredAt: LocalDateTime = LocalDateTime.now()
    var activatedAt: LocalDateTime? = null
        protected set
    var deactivatedAt: LocalDateTime? = null
        protected set

    internal fun setActivatedAt() {
        if (activatedAt != null) {
            throw IllegalStateException("이미 activatedAt 이 설정되었습니다.")
        }
        activatedAt = LocalDateTime.now()
    }

    internal fun deactivate() {
        if (deactivatedAt != null) {
            throw IllegalStateException("이미 deactivatedAt 이 설정되었습니다.")
        }
        deactivatedAt = LocalDateTime.now()
    }

    fun updateInfo(updateRequest: MemberInfoUpdateRequest) {
        profile = Profile(updateRequest.profileAddress)
        introduction = updateRequest.introduction
    }
}
