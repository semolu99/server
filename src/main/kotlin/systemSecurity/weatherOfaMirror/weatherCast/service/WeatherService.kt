package systemSecurity.weatherOfaMirror.weatherCast.service


import com.google.gson.Gson
import jakarta.transaction.Transactional
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import systemSecurity.weatherOfaMirror.core.annotation.Apikey
import systemSecurity.weatherOfaMirror.core.exception.InvalidInputException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class WeatherService(
    apikey: Apikey,
    private val locationService: LocationService,
    private val coordinateService: CoordinateService
) {
    val shelterApikey: String = apikey.getShelterApikey()
    val disasterApikey: String = apikey.getDisasterApikey()
    val whetherApikey: String = apikey.getWhetherApikey()
    val LiveApikey: String = apikey.getLiveApikey()
    val AWSApikey: String = apikey.getAWSApikey()

    fun shortTerm(area: String): Map<*, *>? {
        val localtime = LocalDateTime.now().toString()
        val splitTime = localtime.split("-")
        val day = splitTime[2].split("T")
        val time = day[1].split(":")
        val now = splitTime[0] + splitTime[1] + day[0]
        val nowTime = time[0] + time[1]
        val coordinates = locationService.fetchCoordinatesFromAddress(area)?:throw InvalidInputException("지역","오류")
        println(coordinates.x.toInt())
        println(coordinates.y.toInt())
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
                    .queryParam("base_date", now)
                    .queryParam("base_time", nowTime)
                    .queryParam("nx", coordinates.x.toInt())
                    .queryParam("ny", coordinates.y.toInt())
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        val gson = Gson()
        return gson.fromJson(result, Map::class.java)
    }

    fun shelter(area: String): Map<*, *>? {
        val coordinates = coordinateService.coordinatesFromAddress(area)?:throw InvalidInputException("지역","오류")
        val startLot = String.format("%.6f", coordinates.first - 0.1)
        val startLat = String.format("%.6f", coordinates.second - 0.1)
        val endLot = String.format("%.6f", coordinates.first + 0.1)
        val endLat = String.format("%.6f", coordinates.second + 0.1)
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://www.safetydata.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        println(coordinates.first)
        println(coordinates.second)
        val response = webClient
            .get()
            .uri {
                it.path("/V2/api/DSSP-IF-10941")
                    .queryParam("serviceKey", shelterApikey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 30)
                    .queryParam("returnType", "JSON")
                    .queryParam("startLot", startLot)
                    .queryParam("startLat", startLat)
                    .queryParam("endLot", endLot)
                    .queryParam("endLat", endLat)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        val gson = Gson()
        println(coordinates.first + 0.1)
        println(coordinates.second+0.1)
        return gson.fromJson(result, Map::class.java)
    }

    fun disasterMsg(area: String): Map<*,*>? {
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
                    .queryParam("returnType","JSON")
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 30)
                    .queryParam("crtDt", now)
                    .queryParam("rgnNm",area)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        val gson = Gson()
        println(area)
        println(now)
        return gson.fromJson(result, Map::class.java)
    }

    fun weekWeather(area: String): Map<*, *>? {
        val coordinates = coordinateService.coordinatesFromAddress(area)?:throw InvalidInputException("지역","오류")

        println(coordinates.first)
        println(coordinates.second)

        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://api.open-meteo.com/v1/forecast")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.queryParam("longitude", coordinates.first)
                    .queryParam("latitude", coordinates.second)
                    .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_probability_max")
                    .queryParam("timezone", "Asia/Seoul")
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        val result = response.block()
        val gson = Gson()
        return gson.fromJson(result, Map::class.java)
    }

    fun earthQuake(/*earthQuakeDto: EarthQuakeDto*/): String? {
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("api/typ01/url/eqk_now.php")
                    .queryParam("authKey", whetherApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        return response.block()
    }

    fun typhoon(/*typhoonDto: TyphoonDto*/): String? {
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("api/typ01/url/typ_now.php")
                    .queryParam("authKey", whetherApikey)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
        return response.block()
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
        return response.block()
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
        return response.block()
    }
}