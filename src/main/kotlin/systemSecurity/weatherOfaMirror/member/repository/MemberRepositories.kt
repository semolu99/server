package systemSecurity.weatherOfaMirror.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import systemSecurity.weatherOfaMirror.member.dto.MemberDtoResponse
import systemSecurity.weatherOfaMirror.member.dto.MemberMirrorDtoResponse
import systemSecurity.weatherOfaMirror.member.entity.Member
import systemSecurity.weatherOfaMirror.member.entity.MemberRole
import systemSecurity.weatherOfaMirror.member.entity.Mirror

interface MemberRepository : JpaRepository<Member, Long>{
    //ID 중복 검사용
    fun findByLoginId(LoginId: String): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long>

interface MemberMirrorRepository : JpaRepository<Mirror, Long>{
    fun findByMirrorCode(mirrorCode:String): Mirror?
    fun findMirrorById(id:Long): Mirror?
    fun findAllByMemberId(id:Long): List<MemberMirrorDtoResponse>?
    fun deleteMirrorByMemberIdAndId(id:Long, mirrorId:Long)
}