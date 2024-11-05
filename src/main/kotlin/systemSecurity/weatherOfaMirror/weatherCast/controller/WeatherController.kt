package systemSecurity.weatherOfaMirror.weatherCast.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import systemSecurity.weatherOfaMirror.weatherCast.dto.DisasterMsgDto
import systemSecurity.weatherOfaMirror.weatherCast.dto.ShelterDto
import systemSecurity.weatherOfaMirror.weatherCast.dto.WeatherDto
import systemSecurity.weatherOfaMirror.weatherCast.service.WeatherService

@RestController
@RequestMapping("/mirror/weather")
class WeatherController(
    private val weatherService: WeatherService
) {
    @GetMapping("/shortTerm")
    fun shortTerm(/*@RequestBody weatherDto: WeatherDto*/):String?{
        val result:String? = weatherService.shortTerm(/*weatherDto*/)
        return result
    }

    @GetMapping("/shelter")
    fun shelter(/*@RequestBody shelterDto: ShelterDto*/):String?{
        val result:String? = weatherService.shelter(/*shelterDto*/)
        return result
    }

    @GetMapping("/disasterMsg")
    fun disasterMsg(/*@RequestBody disasterMsgDto: DisasterMsgDto*/):String?{
        val result:String? = weatherService.disasterMsg(/*DisasterMsgDto*/)
        return result
    }

    @GetMapping("/earthQuake")
    fun earthQuake(/*@RequestBody earthQuakeDto: EarthQuakeDto*/):String?{
        val result:String? = weatherService.earthQuake(/*EarthQuakeDto*/)
        return result
    }

    @GetMapping("/typhoon")
    fun typhoon(/*@RequestBody typhoonDto: TyphoonDto*/):String?{
        val result:String? = weatherService.typhoon(/*TyphoonDto*/)
        return result
    }

    @GetMapping("/aws")
    fun aws(/*@RequestBody awsDto: AwsDto*/):String?{
        val result:String? = weatherService.aws(/*AwsDto*/)
        return result
    }

    @GetMapping("/live")
    fun live(/*@RequestBody liveDto: LiveDto*/):String?{
        val result:String? = weatherService.live(/*LiveDto*/)
        return result
    }
}