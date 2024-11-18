package systemSecurity.weatherOfaMirror.core.global

import org.springframework.stereotype.Component
import kotlin.math.*

@Component
class LambertProjection {
    private val PI = Math.PI
    private val DEGRAD = PI / 180.0
    private val RADDEG = 180.0 / PI

    // Lambert 파라미터
    data class LamcParameter(
        var Re: Double = 6371.00877, // 사용할 지구반경 [ km ]
        var grid: Double = 5.0,      // 격자간격 [ km ]
        var slat1: Double = 30.0,    // 표준위도 [degree]
        var slat2: Double = 60.0,    // 표준위도 [degree]
        var olon: Double = 126.0,    // 기준점의 경도 [degree]
        var olat: Double = 38.0,     // 기준점의 위도 [degree]
        var xo: Double = 210 / 5.0,  // 기준점 X좌표 [격자거리]
        var yo: Double = 675 / 5.0,  // 기준점 Y좌표 [격자거리]
        var first: Boolean = false   // 시작 여부 (false = 시작)
    )

    data class Coordinates(var lon: Double, var lat: Double)
    data class GridCoordinates(var x: Double, var y: Double)

    private val map = LamcParameter()

    fun convertToGrid(coord: Coordinates): GridCoordinates {
        val gridCoords = GridCoordinates(0.0, 0.0)
        lamcproj(coord.lon, coord.lat, gridCoords, 0)
        gridCoords.x = (gridCoords.x + 1.5).toInt().toDouble()
        gridCoords.y = (gridCoords.y + 1.5).toInt().toDouble()
        return gridCoords
    }

    private fun lamcproj(input1: Double, input2: Double, result: Any, code: Int) {
        val re = map.Re / map.grid
        val slat1 = map.slat1 * DEGRAD
        val slat2 = map.slat2 * DEGRAD
        val olon = map.olon * DEGRAD
        val olat = map.olat * DEGRAD

//        if (!map.first) {
//            val sn = ln(cos(slat1) / cos(slat2)) / ln(tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5))
//            val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
//            val ro = re * sf / tan(PI * 0.25 + olat * 0.5).pow(sn)
//            map.first = true
//        }

        val sn = ln(cos(slat1) / cos(slat2)) / ln(tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5))
        val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
        val ro = re * sf / tan(PI * 0.25 + olat * 0.5).pow(sn)

        if (code == 0) {
            val ra = re * sf / tan(PI * 0.25 + input2 * DEGRAD * 0.5).pow(sn)
            var theta = input1 * DEGRAD - olon
            if (theta > PI) theta -= 2.0 * PI
            if (theta < -PI) theta += 2.0 * PI
            theta *= sn
            if (result is GridCoordinates) {
                result.x = ra * sin(theta) + map.xo
                result.y = ro - ra * cos(theta) + map.yo
            }
        } else {
            val xn = input1 - map.xo
            val yn = ro - input2 + map.yo
            val ra = sqrt(xn * xn + yn * yn)
            val alat = 2.0 * atan((re * sf / ra).pow(1.0 / sn)) - PI * 0.5
            val theta = if (abs(xn) <= 0.0) 0.0 else atan2(xn, yn)
            val alon = theta / sn + olon
            if (result is Coordinates) {
                result.lon = alon * RADDEG
                result.lat = alat * RADDEG
            }
        }
    }
}