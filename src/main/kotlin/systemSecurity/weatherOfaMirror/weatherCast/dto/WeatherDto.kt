package systemSecurity.weatherOfaMirror.weatherCast.dto


data class AreaPointDtoRequest(
    val area: String?
)

class MapXYDtoRequest(
    val x: String,
    val y: String
)

data class WeatherDto(
    val pageNo: String?,
    val numOfRows: String?,
    val dataType: String?,
    val base_date: String?,
    val base_time: String?,
    val nx: String?,
    val ny: String?,
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
