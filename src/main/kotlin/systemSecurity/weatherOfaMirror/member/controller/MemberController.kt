package systemSecurity.weatherOfaMirror.member.controller

import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import systemSecurity.weatherOfaMirror.core.authority.TokenInfo
import systemSecurity.weatherOfaMirror.core.dto.BaseResponse
import systemSecurity.weatherOfaMirror.core.dto.CustomUser
import systemSecurity.weatherOfaMirror.member.dto.*
import systemSecurity.weatherOfaMirror.member.service.MemberService

@RestController
@RequestMapping("/mirror/member")
class MemberController(
    private val memberService: MemberService
) {
    /**
     * 회원 가입
     */
    @PostMapping("signup")
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit>{
        val resultMsg : String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }
    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }
    /**
     * 내 정보 보기
     */
    @GetMapping("/info")
    fun searchMyInfo():BaseResponse<MemberDtoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }
    /**
     * 거울 등록
     */
    @PostMapping("/mirror/add")
    fun addMirror(@RequestBody @Valid mirrorDtoRequest: MemberMirrorDtoRequest):BaseResponse<Unit>{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val result = memberService.addMirror(mirrorDtoRequest, userId)
        return BaseResponse(message = result)
    }
    /**
     * 거울 삭제
     */
    @DeleteMapping("/mirror/{mirrorId}")
    fun delMirror(@PathVariable mirrorId:Long):BaseResponse<String>{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val result = memberService.delMirror(userId, mirrorId)
        return BaseResponse(message = result)
    }
    /**
     * 거울 리스트
     */
    @GetMapping("/mirror")
    fun searchMyMirror():BaseResponse<List<MemberMirrorDtoResponse>>{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = memberService.searchMyMirror(userId)
        return BaseResponse(data = response)
    }
}