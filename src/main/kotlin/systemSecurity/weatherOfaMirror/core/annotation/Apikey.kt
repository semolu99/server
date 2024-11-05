package systemSecurity.weatherOfaMirror.core.annotation

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Apikey {
    @Value("\${apiKey.ShelterApiKey}")
    lateinit var ShelterApiKey: String

    @Value("\${apiKey.DisasterApiKey}")
    lateinit var DisasterApiKey: String

    @Value("\${apiKey.WhetherApiKey}")
    lateinit var WhetherApiKey: String

    @Value("\${apiKey.AWSApiKey}")
    lateinit var AWSApiKey: String

    @Value("\${apiKey.LiveApiKey}")
    lateinit var LiveApiKey: String

    fun getShelterApikey(): String {
        return ShelterApiKey
    }

    fun getDisasterApikey(): String {
        return DisasterApiKey
    }

    fun getWhetherApikey(): String {
        return WhetherApiKey
    }

    fun getAWSApikey(): String {
        return AWSApiKey
    }

    fun getLiveApikey(): String {
        return LiveApiKey
    }

}