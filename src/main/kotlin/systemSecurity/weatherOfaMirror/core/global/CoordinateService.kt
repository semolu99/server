package systemSecurity.weatherOfaMirror.core.global

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.transaction.Transactional
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import systemSecurity.weatherOfaMirror.core.annotation.Apikey
import systemSecurity.weatherOfaMirror.core.exception.InvalidInputException

@Service
@Transactional
class CoordinateService(
    apikey: Apikey
) {
    private val coordinateApiKey: String = apikey.coordinateApiKey
    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    fun coordinatesFromAddress(area: String): Pair<Double, Double>? {
        val url = WebClient
            .builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

        val response = url
            .get()
            .uri { it.path("v2/local/search/address.json")
                .queryParam("query", area)
                .build() }
            .header("Authorization", "KakaoAK $coordinateApiKey")
            .retrieve()
            .bodyToMono<String>()
            .block()

        response?.let {
            val kakaoResponse: KakaoAddressResponse = objectMapper.readValue(it)
            val firstDocument = kakaoResponse.documents.firstOrNull()
                ?: throw InvalidInputException("ERROR","지역 없음")
            return Pair(firstDocument.x, firstDocument.y)
        }
        return null
    }
}

