package systemSecurity.weatherOfaMirror.weatherCast.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import systemSecurity.weatherOfaMirror.core.annotation.Apikey
import java.time.LocalDateTime

@Service
@Transactional
class WeatherService(
    apikey: Apikey,
    private val locationService: LocationService
) {
    val shelterApikey: String = apikey.getShelterApikey()
    val disasterApikey: String = apikey.getDisasterApikey()
    val whetherApikey: String = apikey.getWhetherApikey()
    val LiveApikey: String = apikey.getLiveApikey()
    val AWSApikey: String = apikey.getAWSApikey()

    fun shortTerm(area: String): String? {
        val localtime = LocalDateTime.now().toString()
        val splitTime = localtime.split("-")
        val day = splitTime[2].split("T")
        val time = day[1].split(":")
        val now = splitTime[0] + splitTime[1] + day[0]
        val nowTime = time[0]+time[1]

        val coordinates = locationService.fetchCoordinatesFromAddress(area)
        println(coordinates?.x)
        println(coordinates?.y)
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("api/typ02/openApi/VilageFcstInfoService_2.0/getUltraSrtNcst")
                    .queryParam("authKey", whetherApikey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 1000)
                    .queryParam("dataType", "JSON")
                    .queryParam("base_date",now)
                    .queryParam("base_time", nowTime)
                    .queryParam("nx",coordinates?.x)
                    .queryParam("ny",coordinates?.y)
                .build()
            }
            .retrieve()
            .bodyToMono<String>()
        println(response.block())

        val result = response.block()
        return result
    }

    fun shelter(/*shelterDto: ShelterDto*/): String? {
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://www.safetydata.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("/V2/api/DSSP-IF-10941")
                    .queryParam("serviceKey", shelterApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()
        return result
    }

    fun disasterMsg(/*shelterDto: ShelterDto*/): String? {
        val localtime = LocalDateTime.now().toString()
        val splitTime = localtime.split("-")
        val now = splitTime[0] + splitTime[1] + splitTime[2].split("T")[0]
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://www.safetydata.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("/V2/api/DSSP-IF-00247")
                    .queryParam("serviceKey", disasterApikey)
                    .queryParam("crtDt", now)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        return result
    }

    fun earthQuake(/*earthQuakeDto: EarthQuakeDto*/):String? {
        val webClient : WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri{
                it.path("api/typ01/url/eqk_now.php")
                    .queryParam("authKey",whetherApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        return result
    }

    fun typhoon(/*typhoonDto: TyphoonDto*/):String? {
        val webClient : WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri{
                it.path("api/typ01/url/typ_now.php")
                    .queryParam("authKey",whetherApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        return result
    }

    fun aws(/*awsDto: AWSDto*/): String? {
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://www.safetydata.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("/V2/api/DSSP-IF-00026")
                    .queryParam("serviceKey", AWSApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        return result
    }

    fun live(/*liveDto: LiveDto*/): String? {
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://www.safetydata.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("/V2/api/DSSP-IF-00183")
                    .queryParam("serviceKey", LiveApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        return result
    }
}