package systemSecurity.weatherOfaMirror.core.status

enum class Area(val des: String){
    SEOU("서울"),
    BUSA("부산"),
    DAEG("대구"),
    INCH("인천"),
    GWAN("광주"),
    DAEJ("대전"),
    ULSA("울산"),
    SEJO("세종"),
    GYEO("경기"),
    GANG("강원"),
    CHUB("충북"),
    CHUN("충남"),
    JEOB("전북"),
    JEON("전남"),
    GYUB("경북"),
    GYUN("경남"),
    JEJU("제주")
}

enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다.")
}

enum class ROLE{
    MEMBER
}
