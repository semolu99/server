package systemSecurity.weatherOfaMirror.member.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.stereotype.Service
import systemSecurity.weatherOfaMirror.core.authority.JwtTokenProvider
import systemSecurity.weatherOfaMirror.core.authority.TokenInfo
import systemSecurity.weatherOfaMirror.core.exception.InvalidInputException
import systemSecurity.weatherOfaMirror.core.status.ROLE
import systemSecurity.weatherOfaMirror.core.status.ResultCode
import systemSecurity.weatherOfaMirror.member.dto.*
import systemSecurity.weatherOfaMirror.member.entity.Member
import systemSecurity.weatherOfaMirror.member.entity.MemberRole
import systemSecurity.weatherOfaMirror.member.entity.Mirror
import systemSecurity.weatherOfaMirror.member.repository.MemberMirrorRepository
import systemSecurity.weatherOfaMirror.member.repository.MemberRepository
import systemSecurity.weatherOfaMirror.member.repository.MemberRoleRepository

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val mirrorRepository: MemberMirrorRepository
) {
    /**
     * 회원가입
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String{
        var member: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if(member != null){
            throw InvalidInputException(ResultCode.ERROR.name, message = "중복된 ID 입니다.")
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
        var member: Member = memberRepository.findByLoginId(loginDto.loginId)
            ?: throw InvalidInputException(message = "아이디 혹은 비밀번호 오류")
        var encoder = SCryptPasswordEncoder(16,8,1,32,64)
        if(!encoder.matches(loginDto.password,member.password)){
            throw InvalidInputException(ResultCode.ERROR.toString(), message = "아이디 혹은 비밀번호 오류")
        }
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, member.password)
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
    /**
     * 거울 등록
     */
    fun addMirror(mirrorDtoRequest: MemberMirrorDtoRequest, id:Long): String {
        val member:Member = memberRepository.findByIdOrNull(id)
            ?:throw InvalidInputException("ERROR",message = "잘못된 회원 번호")
        var mirror : Mirror? = mirrorRepository.findByMirrorCode(mirrorDtoRequest.mirrorCode)
        if(mirror != null) throw InvalidInputException("ERROR",message = "이미 등록된 거울입니다.")
        mirror = Mirror(null, mirrorDtoRequest.mirrorCode, mirrorDtoRequest.mirrorName ,member)
        mirrorRepository.save(mirror)
        return "등록 완료"
    }
    /**
     * 거울 삭제
     */
    fun delMirror(id : Long, mirrorId: Long):String{
        val mirror : Mirror = mirrorRepository.findMirrorById(mirrorId) ?: throw InvalidInputException("ERROR",message = "존재 하지 않는 거울입니다..")
        if(mirror.member.id != id){
            throw InvalidInputException("ERROR",message = "존재 하지 않는 거울입니다.")
        }
        mirrorRepository.deleteMirrorByMemberIdAndId(id, mirrorId)
        return "회원님의 ${mirror.mirrorName}이(가) 삭제 되었습니다."
    }
    /**
     * 거울 정보 리스트
     */
    fun searchMyMirror(id:Long):List<MemberMirrorDtoResponse>?{
        return mirrorRepository.findAllByMemberId(id)
    }
}