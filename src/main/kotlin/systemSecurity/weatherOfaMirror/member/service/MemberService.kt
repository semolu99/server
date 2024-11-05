package systemSecurity.weatherOfaMirror.member.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import systemSecurity.weatherOfaMirror.core.authority.JwtTokenProvider
import systemSecurity.weatherOfaMirror.core.authority.TokenInfo
import systemSecurity.weatherOfaMirror.core.exception.InvalidInputException
import systemSecurity.weatherOfaMirror.core.status.ROLE
import systemSecurity.weatherOfaMirror.core.status.ResultCode
import systemSecurity.weatherOfaMirror.member.dto.LoginDto
import systemSecurity.weatherOfaMirror.member.dto.MemberDtoRequest
import systemSecurity.weatherOfaMirror.member.dto.MemberDtoResponse
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
    fun signUp(memberDtoRequest: MemberDtoRequest): String{
        var member: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if(member != null){
            return "이미 등록된 ID 입니다."
        }

        member = memberDtoRequest.toEntity()
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
    /**
     * 내 정보 보기
     */
    fun searchMyInfo(id: Long):MemberDtoResponse{
        val member : Member = memberRepository.findByIdOrNull(id)?: throw InvalidInputException("id","해당 회원 번호는 존재 하지 않은 유저입니다.")
        return member.toDto()
    }
}