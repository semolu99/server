package systemSecurity.weatherOfaMirror.core.global

/*import org.springframework.stereotype.Component
import systemSecurity.weatherOfaMirror.weatherCast.dto.MapXYDtoRequest
import kotlin.math.*

@Component
class ConvertGPS {
    val RE = 6371.00877 // 지구 반경
    val GRID = 5.0 // 격자 간격
    val SLAT1 = 30.0 // 표준 위도
    val SLAT2 = 60.0 // 표준 위도
    val OLON = 126.0 // 기준점 경도
    val OLAT = 38.0 // 기준점 위도
    val XO = 210 / GRID // 기준점 X 좌표
    val YO = 675 / GRID // 기준점 Y 좌표

    fun convertGPStoXY(latitude: Double, longitude: Double): MapXYDtoRequest {
        val map = lamcproj(latitude, longitude)
        val x = (map["x"]!! + 1.5).toInt()
        val y = (map["y"]!! + 1.5).toInt()
        println("/////////////${x}")
        println("/////////latitude${latitude}")
        println("/////////////${map["x"]}")
        return MapXYDtoRequest(x=x.toString(),y=y.toString())
    }

    /** Lambert Conformal Conic Projection **/
    private fun lamcproj(latitude: Double, longitude: Double): Map<String, Double> {
        val PI = asin(1.0) * 2.0
        val DEGRAD = PI / 180.0

        val radius = RE / GRID
        val standardLatitude1 = SLAT1 * DEGRAD
        val standardLatitude2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = tan(PI * 0.25 + standardLatitude2 * 0.5) / tan(PI * 0.25 + standardLatitude1 * 0.5)
        sn = ln(cos(standardLatitude1) / cos(standardLatitude2)) / ln(sn)
        var sf = tan(PI * 0.25 + standardLatitude1 * 0.5)
        sf = sf.pow(sn) * cos(standardLatitude1) / sn
        var ro = tan(PI * 0.25 + olat * 0.5)
        ro = radius * sf / ro.pow(sn)
        var rastart = tan(PI * 0.25 + latitude * DEGRAD * 0.5)
        //println("first ra : $ra\nsf : $sf\n ra.pow : ${ra.pow(sn)}\nsn : $sn\nro : $ro")
        val absRa = rastart.absoluteValue
        val rafinal  = radius * sf / absRa.pow(sn)
        println("radius : $radius\nsf : $sf")

        val ra = if (rastart < 0) -rafinal else rafinal

        var theta = longitude * DEGRAD - olon
        if (theta > PI)
            theta -= 2.0 * PI
        if (theta < -PI)
            theta += 2.0 * PI
        theta *= sn

        val x = (ra * sin(theta)) + XO
        val y = (ro - ra * cos(theta)) + YO
        println("ra : $ra\nsin(theta) : ${sin(theta)}\ncos(theta) : ${cos(theta)}\ntheta : $theta")
        println("X : $x")
        println("Y : $y")

        return mapOf("x" to x, "y" to y)
    }
}
import org.springframework.stereotype.Component
import kotlin.math.*

const val COEFFICIENT_TO_RADIAN = Math.PI / 180.0
const val GRID_UNIT_COUNT = 6371.00877 / 5.0  // 지구 반지름 ÷ 정방형 격자 단위 길이 = 격자 개수
const val REF_X = 43.0 // 기준점 X좌표
const val REF_Y = 136.0 // 기준점 Y좌표
const val REF_LON_RAD = 126.0 * COEFFICIENT_TO_RADIAN // 기준점 경도 (rad)
const val REF_LAT_RAD = 38.0 * COEFFICIENT_TO_RADIAN // 기준점 위도 (rad)
const val PROJ_LAT_1_RAD = 30.0 * COEFFICIENT_TO_RADIAN // 투영 위도1 (rad)
const val PROJ_LAT_2_RAD = 60.0 * COEFFICIENT_TO_RADIAN // 투영 위도2 (rad)


data class CoordinatesXy(val nx: Int, val ny: Int)
data class CoordinatesLatLon(val lat: Double, val lon: Double)

@Component
class CoordinateConverter {
    private val sn = ln(cos(PROJ_LAT_1_RAD) / cos(PROJ_LAT_2_RAD)) / ln(tan(Math.PI * 0.25 + PROJ_LAT_2_RAD * 0.5) / tan(Math.PI * 0.25 + PROJ_LAT_1_RAD * 0.5))
    private val sf = tan(Math.PI * 0.25 + PROJ_LAT_1_RAD * 0.5).pow(sn) * cos(PROJ_LAT_1_RAD) / sn
    private val ro = GRID_UNIT_COUNT * sf / tan(Math.PI * 0.25 + REF_LAT_RAD * 0.5).pow(sn)
    /**
     * Returns the corresponding Cartesian coordinates of the point of [lat] and [lon].
     */
    internal fun convertToXy(lat: Double, lon: Double): CoordinatesXy {
        // `ra` 계산
        val tanInput = Math.PI * 0.25 + lat * COEFFICIENT_TO_RADIAN * 0.5
        val tanValue = tan(tanInput)

        // tan 값의 절대값으로 pow 계산
        val powInput = tanValue.absoluteValue.pow(sn)

        // 최종 ra 계산
        val ra = GRID_UNIT_COUNT * sf / powInput * if (tanValue < 0) -1 else 1
        //val ra = GRID_UNIT_COUNT * sf / tan(Math.PI * 0.25 + lat * COEFFICIENT_TO_RADIAN * 0.5).pow(sn)
        val theta: Double = lon * COEFFICIENT_TO_RADIAN - REF_LON_RAD
        val niceTheta = if (theta < -Math.PI) {
            theta + 2 * Math.PI
        } else if (theta > Math.PI) {
            theta - 2 * Math.PI
        }
        else theta
        println("change"+floor(ra * sin(niceTheta * sn) + REF_X + 0.5).toInt())
        println("change"+floor(ro - ra * cos(niceTheta * sn) + REF_Y + 0.5).toInt())
        return CoordinatesXy(
            nx = floor(ra * sin(niceTheta * sn) + REF_X + 0.5).toInt(),
            ny = floor(ro - ra * cos(niceTheta * sn) + REF_Y + 0.5).toInt()
        )

    }

    /**
     * Returns the corresponding Spherical coordinates of the point of [nx] and [ny].
     */
    internal fun convertToLatLon(nx: Double, ny: Double): CoordinatesLatLon {
        val diffX: Double = nx - REF_X
        val diffY: Double = ro - ny + REF_Y
        val distance = sqrt(diffX * diffX + diffY * diffY)

        // The latitude
        val latSign: Int = if (sn < 0) -1 else 1
        val latRad = 2 * atan((GRID_UNIT_COUNT * sf / distance).pow(1.0 / sn)) - Math.PI * 0.5

        // The longitude
        val theta: Double = if (abs(diffX) <= 0) 0.0 else {
            if (abs(diffY) <= 0) {
                if (diffX < 0) -Math.PI * 0.5 else Math.PI * 0.5
            } else atan2(diffX, diffY)
        }
        val lonRad = theta / sn + REF_LON_RAD

        return CoordinatesLatLon(
            lat = latSign * latRad / COEFFICIENT_TO_RADIAN,
            lon = lonRad / COEFFICIENT_TO_RADIAN
        )
    }
}*/

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

    fun convertToLatLon(grid: GridCoordinates): Coordinates {
        val x = grid.x - 1
        val y = grid.y - 1
        val coord = Coordinates(0.0, 0.0)
        lamcproj(x, y, coord, 1)
        return coord
    }

    private fun lamcproj(input1: Double, input2: Double, result: Any, code: Int) {
        val re = map.Re / map.grid
        val slat1 = map.slat1 * DEGRAD
        val slat2 = map.slat2 * DEGRAD
        val olon = map.olon * DEGRAD
        val olat = map.olat * DEGRAD

        if (!map.first) {
            val sn = ln(cos(slat1) / cos(slat2)) / ln(tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5))
            val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
            val ro = re * sf / tan(PI * 0.25 + olat * 0.5).pow(sn)
            map.first = true
        }

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

fun main(args: Array<String>) {
    val lambert = LambertProjection()

    if (args.size != 3) {
        println("Usage: [0 <longitude> <latitude>] or [1 <X-grid> <Y-grid>]")
        return
    }

    val mode = args[0].toInt()
    val input1 = args[1].toDouble()
    val input2 = args[2].toDouble()

    if (mode == 0) {
        val coordinates = LambertProjection.Coordinates(input1, input2)
        val gridCoordinates = lambert.convertToGrid(coordinates)
        println("lon.= ${coordinates.lon}, lat.= ${coordinates.lat} ---> X = ${gridCoordinates.x.toInt()}, Y = ${gridCoordinates.y.toInt()}")
    } else if (mode == 1) {
        val gridCoordinates = LambertProjection.GridCoordinates(input1, input2)
        val coordinates = lambert.convertToLatLon(gridCoordinates)
        println("X = ${gridCoordinates.x.toInt()}, Y = ${gridCoordinates.y.toInt()} ---> lon.= ${coordinates.lon}, lat.= ${coordinates.lat}")
    } else {
        println("Invalid mode. Use 0 for lon/lat to grid or 1 for grid to lon/lat.")
    }
}

