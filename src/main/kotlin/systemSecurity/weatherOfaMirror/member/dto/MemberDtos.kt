package systemSecurity.weatherOfaMirror.member.dto

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import systemSecurity.weatherOfaMirror.core.annotation.ValidEnum
import systemSecurity.weatherOfaMirror.core.status.Area
import systemSecurity.weatherOfaMirror.member.entity.Member

data class MemberDtoRequest(
    val id: Long?,

    @field:NotBlank
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @field:Pattern(
        regexp="^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수 문자를 포함한 8~20 자리로 입력 해주세요"
    )
    @JsonProperty("password")
    private val _password: String?,

    @field:NotBlank
    @JsonProperty("name")
    private val _name: String?,

    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    private val _email: String?,

    @field:NotBlank
    @field:ValidEnum(enumClass = Area::class,
        message = "해당 코드로 입력 해 주세요.")
    @JsonProperty("area")
    private val _area: String?,
) {
    val encoder = SCryptPasswordEncoder(16,8,1,32,64)

    val loginId: String
        get() = _loginId!!

    val password: String
        get() = encoder.encode(_password!!)

    val name: String
        get() = _name!!

    val email: String
        get() = _email!!

    val area: Area
        get() = Area.valueOf(_area!!)

    fun toEntity(): Member =
        Member(id, loginId, password, name, email, area)
}

data class LoginDto(
    @field:NotBlank
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,
) {
    val loginId: String
        get() = _loginId!!

    val password: String
        get() = _password!!
}

data class MemberDtoResponse(
    val id: Long,
    val loginId: String,
    val name: String,
    val email: String,
    val area: String
)

data class MemberMirrorDtoRequest(
    val id : Long,

    @field:NotBlank
    @JsonProperty("mirrorCode")
    private val _mirrorCode: String?,

    @field:NotBlank
    @JsonProperty("mirrorName")
    private val _mirrorName: String?,
){
    val mirrorCode : String
        get() = _mirrorCode!!

    val mirrorName :String
        get() = _mirrorName!!
}

data class MemberMirrorDtoResponse(
    val id:Long,
    val mirrorCode:String,
    val mirrorName:String
)