package systemSecurity.weatherOfaMirror.weatherCast.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import systemSecurity.weatherOfaMirror.core.dto.BaseResponse
import systemSecurity.weatherOfaMirror.core.dto.CustomUser
import systemSecurity.weatherOfaMirror.core.exception.InvalidInputException
import systemSecurity.weatherOfaMirror.member.repository.MemberMirrorRepository
import systemSecurity.weatherOfaMirror.member.repository.MemberRepository
import systemSecurity.weatherOfaMirror.weatherCast.service.WeatherMapService
import systemSecurity.weatherOfaMirror.weatherCast.service.WeatherService

@RestController
@RequestMapping("/mirror/weather")
class WeatherController(
    private val weatherService: WeatherService,
    private val mirrorRepository: MemberMirrorRepository,
    private val memberRepository: MemberRepository,
    private val weatherMapService: WeatherMapService
) {
    /**
     * 단기 예보 조회
     */
    @GetMapping("/shortTerm/{area}") //검색
    fun searchShortTerm(@PathVariable area: String): BaseResponse<Map<*,*>>? {
        return BaseResponse(data = weatherService.shortTerm(area))
    }

    @GetMapping("/shortTerm") //member
    fun memberShortTerm(): BaseResponse<Map<*,*>>? {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val member = memberRepository.findMemberById(userId)?: throw InvalidInputException("member","존재 하지 않는 멤버")
        return BaseResponse(data = weatherService.shortTerm(member.area.des))
    }

    @GetMapping("/mirror/shortTerm") //mirror
    fun mirrorShortTerm(@RequestHeader("mirrorCode") mirrorCode:String):Map<*,*>?{
        val mirror = mirrorRepository.findByMirrorCode(mirrorCode) ?: throw InvalidInputException("mirror code","존재하지 않는 정보입니다.")
        return weatherService.shortTerm(mirror.member.area.des)
    }

    @GetMapping("/shelter/{area}")//대피소 정보 검색
    fun searchShelter(@PathVariable area: String): BaseResponse<Map<*,*>>?{
        return BaseResponse(data = weatherService.shelter(area))
    }

    @GetMapping("/shelter")//대피소 정보 회원
    fun memberShelter(): BaseResponse<Map<*,*>>?{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val member = memberRepository.findMemberById(userId)?: throw InvalidInputException("member","존재 하지 않는 멤버")
        return BaseResponse(data = weatherService.shelter(member.area.des))
    }

    @GetMapping("/disasterMsg")//재난 문자
    fun disasterMsg(): BaseResponse<Map<*,*>>? {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val member = memberRepository.findMemberById(userId)?: throw InvalidInputException("member","존재 하지 않는 멤버")
        return BaseResponse(data = weatherService.disasterMsg(member.area.des))
    }

    @GetMapping("/weatherMapService")//지도 날씨 출력 회원
    fun weatherMapService(): BaseResponse<Map<*,*>>? {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        memberRepository.findMemberById(userId)?: throw InvalidInputException("member","존재 하지 않는 멤버")
        return BaseResponse(data = weatherMapService.weatherMapService())
    }

    @GetMapping("/weatherMapService/mirror")//지도 날씨 출력 거울
    fun mirrorWeatherMapService(@RequestHeader("mirrorCode") mirrorCode:String): BaseResponse<Map<*,*>>? {
        mirrorRepository.findByMirrorCode(mirrorCode) ?: throw InvalidInputException("mirror code","존재하지 않는 정보입니다.")
        return BaseResponse(data = weatherMapService.weatherMapService())
    }

    @GetMapping("/weekWeather")//일주일 날씨 정보 회원
    fun weekWeather(): BaseResponse<Map<*,*>>?{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val member = memberRepository.findMemberById(userId)?: throw InvalidInputException("member","존재 하지 않는 멤버")
        return BaseResponse(data = weatherService.weekWeather(member.area.des))
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