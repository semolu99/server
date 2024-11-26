package systemSecurity.weatherOfaMirror.core.annotation

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Apikey {
    @Value("\${apiKey.ShelterApiKey}")
    lateinit var shelterApiKey: String

    @Value("\${apiKey.DisasterApiKey}")
    lateinit var disasterApiKey: String

    @Value("\${apiKey.WhetherApiKey}")
    lateinit var whetherApiKey: String

    @Value("\${apiKey.AWSApiKey}")
    lateinit var aWSApiKey: String

    @Value("\${apiKey.LiveApiKey}")
    lateinit var liveApiKey: String

    @Value("\${apiKey.KakaoApiKey}")
    lateinit var coordinateApiKey: String

    @Value("\${apiKey.DustApiKey}")
    lateinit var dustApiKey:String
}