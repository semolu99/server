package systemSecurity.weatherOfaMirror.member.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import systemSecurity.weatherOfaMirror.core.authority.TokenInfo
import systemSecurity.weatherOfaMirror.core.dto.BaseResponse
import systemSecurity.weatherOfaMirror.member.dto.LoginDto
import systemSecurity.weatherOfaMirror.member.dto.MemberDtoRequset
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
    fun signUp(@RequestBody @Valid memberDtoRequset: MemberDtoRequset): BaseResponse<Unit>{
        val resultMsg : String = memberService.signUp(memberDtoRequset)
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
}