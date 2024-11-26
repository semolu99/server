package systemSecurity.weatherOfaMirror.weatherCast.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import systemSecurity.weatherOfaMirror.core.dto.BaseResponse
import systemSecurity.weatherOfaMirror.core.dto.CustomUser
import systemSecurity.weatherOfaMirror.core.exception.InvalidInputException
import systemSecurity.weatherOfaMirror.core.global.WeatherMapService
import systemSecurity.weatherOfaMirror.member.repository.MemberMirrorRepository
import systemSecurity.weatherOfaMirror.member.repository.MemberRepository
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

    @GetMapping("/weekWeather")
    fun weekWeather():String?{
        val result:String? = weatherService.weekWeather()
        return result
    }

    @GetMapping("/threeWeather")
    fun threeWeather():String?{
        val result:String? = weatherService.threeWeather()
        return result
    }

    @GetMapping("/specialReport")
    fun specialReport():String?{
        val result:String? = weatherService.specialReport()
        return result
    }

    /**
     * 미세 먼지 검색, 특정 지역 X일 경우 전국
     */
    @GetMapping("/dust")
    fun dustInfo(@RequestHeader("area") area: String = "전국"):BaseResponse<Map<*,*>>?{
        return BaseResponse(data=weatherService.dustInfo(area))
    }

    /**
     * member 미세 먼지
     */
    @GetMapping("/dust/member")
    fun memberDustInfo():BaseResponse<Map<*,*>>?{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val member = memberRepository.findMemberById(userId)?: throw InvalidInputException("member","존재 하지 않는 멤버")
        return BaseResponse(data = weatherService.dustInfo(member.area.dust))
    }

    /**
     * member 미세 먼지
     */
    @GetMapping("/dust/mirror")
    fun mirrorDustInfo(@RequestHeader("mirrorCode") mirrorCode:String):BaseResponse<Map<*,*>>?{
        val mirror = mirrorRepository.findByMirrorCode(mirrorCode) ?: throw InvalidInputException("mirror code","존재하지 않는 정보입니다.")
        return BaseResponse(data = weatherService.dustInfo(mirror.member.area.dust))
    }

    /**
     * 전국 미세 먼지 거울 따로 없음
     */
    @GetMapping("/nation/dust")
    fun nationDustInfo():BaseResponse<Map<*,*>>?{
        return BaseResponse(data = weatherService.minuDust())
    }

    /*@GetMapping("/earthQuake")
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
    }*/

}