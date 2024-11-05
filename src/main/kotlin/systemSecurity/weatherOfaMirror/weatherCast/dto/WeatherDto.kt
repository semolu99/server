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

data class DisasterMsgDto(
    val numOfRows: String?,
    val pageNo: String?,
    val returnType: String?,
    val crtDt: String?,
    val rgnNm: String?,
)

data class EarthQuakeDto(
    val tm: String?,
    val disp: String?,
    val help: String?,
)

data class TyphoonDto(
    val YY: String?,
    val disp: String?,
    val help: String,
    val typ: String,
    val seq: String?,
    val mode: String?,
    val tm: String?,
)

data class AwsDto(
    val numOfRows: String?,
    val pageNo: String?,
    val returnType: String?,
    val AWS_OBSVTR_CD: String?,
    val OBSRVN_HR: String?,
)

data class LiveDto(
    val numOfRows: String?,
    val pageNo: String?,
    val returnType: String?,
)
