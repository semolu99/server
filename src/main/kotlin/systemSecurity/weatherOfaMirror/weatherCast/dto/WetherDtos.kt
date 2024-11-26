package systemSecurity.weatherOfaMirror.weatherCast.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class PointShelterDtoRequest(

    @field:NotBlank
    @JsonProperty("xPoint")
    private val _xPoint: String?,

    @field:NotBlank
    @JsonProperty("yPoint")
    private val _yPoint: String?
){
    val xPoint:Double
        get() = _xPoint!!.toDouble()
    val yPoint:Double
        get() = _yPoint!!.toDouble()
}

