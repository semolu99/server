package systemSecurity.weatherOfaMirror.member.service

import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import systemSecurity.weatherOfaMirror.core.authority.JwtTokenProvider
import systemSecurity.weatherOfaMirror.core.authority.TokenInfo
import systemSecurity.weatherOfaMirror.core.status.Area
import systemSecurity.weatherOfaMirror.core.status.ROLE
import systemSecurity.weatherOfaMirror.member.dto.LoginDto
import systemSecurity.weatherOfaMirror.member.dto.MemberDtoRequset
import systemSecurity.weatherOfaMirror.member.entity.Member
import systemSecurity.weatherOfaMirror.member.entity.MemberRole
import systemSecurity.weatherOfaMirror.member.repository.MemberRepository
import systemSecurity.weatherOfaMirror.member.repository.MemberRoleRepository

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    /**
     * 회원가입
     */
    fun signUp(memberDtoRequset: MemberDtoRequset): String{
        var member: Member? = memberRepository.findByLoginId(memberDtoRequset.loginId)
        if(member != null){
            return "이미 등록된 ID 입니다."
        }

        member = memberDtoRequset.toEntity()
        memberRepository.save(member)

        val memberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원 가입 완료"
    }
    /**
     * 로그인
     */
    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }
}