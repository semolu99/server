package systemSecurity.weatherOfaMirror.weatherCast.dto

data class WeatherDto(
    val stn: String?,
    val reg: String?,
    val tmfc: String?,
    val tmfc1: String?,
    val tmfc2: String?,
    val tmef1: String?,
    val tmef2: String?,
    val disp: String?,
    val help: String?,
)

data class ShelterDto(
    val numOfRows: String?,
    val pageNo: String?,
    val startLot: String?,
    val endLot: String?,
    val startLat: String?,
    val endLat: String?,
)