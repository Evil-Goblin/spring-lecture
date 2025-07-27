package com.splearn.application.member.provided

import com.splearn.domain.member.Member

interface MemberFinder {
    fun find(memberId: Long): Member
}
