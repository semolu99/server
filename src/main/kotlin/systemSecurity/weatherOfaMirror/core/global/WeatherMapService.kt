package systemSecurity.weatherOfaMirror.core.global

import com.google.gson.Gson
import jakarta.transaction.Transactional
import org.jsoup.Jsoup
import org.jsoup.Connection
import org.springframework.stereotype.Service

@Service
@Transactional
class WeatherMapService {
    fun weatherMapService(): Map<*,*> {
        val url = "https://weather.naver.com/choiceApi/api?choiceQuery=%7B%22nationFcast%22%3A%7B%22aplYmd%22%3A%2220240416%22%2C%22hdayType%22%3A%22now%22%7D%7D"
        val response = Jsoup.connect(url)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .method(Connection.Method.GET)
            .execute()
            .body()

        /*val weatherData = JSONObject(response)
            .getJSONObject("results")
            .getJSONObject("choiceResult")
            .getJSONObject("nationFcast")

        val result = StringBuilder("전국(일부) 날씨 정보\n\n")
        weatherData.keys().forEach { key ->
            val regionData = weatherData.getJSONObject(key.toString())
            val regionName = regionData.getString("regionName")
            val weatherText = regionData.getString("wetrTxt")
            val temperature = regionData.get("tmpr").toString()

            result.append("이름: $regionName | 상태: $weatherText | 온도: $temperature℃\n\n")
        }*/
        val gson = Gson()
        return gson.fromJson(response, Map::class.java)
    }
}