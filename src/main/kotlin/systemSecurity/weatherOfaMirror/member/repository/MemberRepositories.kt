package systemSecurity.weatherOfaMirror.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import systemSecurity.weatherOfaMirror.member.entity.Member
import systemSecurity.weatherOfaMirror.member.entity.MemberRole

interface MemberRepository : JpaRepository<Member, Long>{
    //ID 중복 검사용
    fun findByLoginId(LoginId: String): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long>