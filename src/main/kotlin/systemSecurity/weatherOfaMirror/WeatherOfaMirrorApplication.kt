package systemSecurity.weatherOfaMirror

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WeatherOfaMirrorApplication

fun main(args: Array<String>) {
	runApplication<WeatherOfaMirrorApplication>(*args)
}