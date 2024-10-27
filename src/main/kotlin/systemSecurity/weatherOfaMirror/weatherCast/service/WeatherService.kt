package systemSecurity.weatherOfaMirror.weatherCast.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import systemSecurity.weatherOfaMirror.core.anntation.Apikey
import systemSecurity.weatherOfaMirror.weatherCast.dto.ShelterDto
import systemSecurity.weatherOfaMirror.weatherCast.dto.WeatherDto

@Service
@Transactional
class WeatherService(
    apikey: Apikey
) {
    val kooApikey: String = apikey.getKooApikey()
    val yungApikey: String = apikey.getYungApikey()

    fun shortTerm(weatherDto : WeatherDto):String? {
        val webClient : WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri{
                it.path("api/typ01/url/fct_shrt_reg.php")
                    .build()
            }
            .header("authKey",kooApikey)
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()
        return result
    }

    fun shelter(shelterDto: ShelterDto):String? {
        val webClient : WebClient = WebClient
            .builder()
            .baseUrl("https://www.safetydata.go.kr/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri{
                it.path("/V2/api/DSSP-IF-10941")
                    .build()
            }
            .header("authKey",yungApikey)
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()
        return result
    }
}