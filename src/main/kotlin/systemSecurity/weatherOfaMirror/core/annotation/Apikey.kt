package systemSecurity.weatherOfaMirror.core.annotation

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Apikey {
    @Value("\${apiKey.kooApiKey}")
    lateinit var kooApiKey: String

    @Value("\${apiKey.yungApiKey}")
    lateinit var yungApiKey: String

    fun getKooApikey(): String {
        return kooApiKey
    }
    fun getYungApikey(): String {
        return yungApiKey
    }
}