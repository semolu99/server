package systemSecurity.weatherOfaMirror.weatherCast.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import systemSecurity.weatherOfaMirror.core.annotation.Apikey
import systemSecurity.weatherOfaMirror.weatherCast.dto.AwsDto
import systemSecurity.weatherOfaMirror.weatherCast.dto.ShelterDto
import systemSecurity.weatherOfaMirror.weatherCast.dto.WeatherDto

@Service
@Transactional
class WeatherService(
    apikey: Apikey
) {
    val shelterApikey: String = apikey.getShelterApikey()
    val disasterApikey: String = apikey.getDisasterApikey()
    val whetherApikey: String = apikey.getWhetherApikey()
    val LiveApikey: String = apikey.getLiveApikey()
    val AWSApikey: String = apikey.getAWSApikey()

    fun shortTerm(/*weatherDto : WeatherDto*/): String? {
        val webClient: WebClient = WebClient
            .builder()
            .baseUrl("https://apihub.kma.go.kr")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        val response = webClient
            .get()
            .uri {
                it.path("api/typ01/url/fct_shrt_reg.php")
                    .build()
            }
            .header("authKey", whetherApikey)
            .retrieve()
            .bodyToMono<String>()

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