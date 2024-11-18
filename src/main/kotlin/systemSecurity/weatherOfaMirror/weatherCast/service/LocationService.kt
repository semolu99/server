package systemSecurity.weatherOfaMirror.weatherCast.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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
import systemSecurity.weatherOfaMirror.core.global.LambertProjection
import systemSecurity.weatherOfaMirror.core.global.LambertProjection.GridCoordinates
//import systemSecurity.weatherOfaMirror.core.global.ConvertGPS
import systemSecurity.weatherOfaMirror.weatherCast.dto.MapXYDtoRequest

@Service
@Transactional
class LocationService(
    apikey: Apikey,
    //private val convertGPS: ConvertGPS,
    private val lambertProjection: LambertProjection
){
    val CoordinateApiKey: String = apikey.getKakaoApiKey()
    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    fun fetchCoordinatesFromAddress(area: String): GridCoordinates? {
        val xPoint : Double
        val yPoint : Double
        val url = WebClient
            .builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

        val response = url
            .get()
            .uri{ it.path("v2/local/search/address.json")
                .queryParam("query", area)
                .build() }
            .header("Authorization", "KakaoAK ${CoordinateApiKey}")
            .retrieve()
            .bodyToMono<String>()
            .block()
        response?.let {
            val kakaoResponse: KakaoAddressResponse = objectMapper.readValue(it)
            val firstDocument = kakaoResponse.documents.firstOrNull()
                ?: throw InvalidInputException()
            xPoint = firstDocument.x
            yPoint = firstDocument.y
            val mapXY=lambertProjection.convertToGrid(LambertProjection.Coordinates(xPoint, yPoint))
            println(mapXY.toString())

            return mapXY
        }

        return null
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddressDocument(
    val address_name: String,
    val x: Double, // 경도 (longitude)
    val y: Double  // 위도 (latitude)
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAddressResponse(
    val documents: List<AddressDocument>
)