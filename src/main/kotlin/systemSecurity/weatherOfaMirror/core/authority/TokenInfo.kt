package systemSecurity.weatherOfaMirror.core.authority

data class TokenInfo(
    val grantType: String,
    val accessToken: String,
)