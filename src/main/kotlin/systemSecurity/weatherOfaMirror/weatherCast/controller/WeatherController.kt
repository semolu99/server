package systemSecurity.weatherOfaMirror.weatherCast.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    fun shelter(@RequestBody shelterDto: ShelterDto):String?{
        val result:String? = weatherService.shelter(shelterDto)
        return result
    }
}