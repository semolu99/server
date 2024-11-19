package systemSecurity.weatherOfaMirror.core.status

enum class Area(val des: String){
    SEOU("서울특별시"),
    BUSA("부산광역시"),
    DAEG("대구광역시"),
    INCH("인천광역시"),
    GWAN("광주광역시"),
    DAEJ("대전광역시"),
    ULSA("울산광역시"),
    SEJO("세종시"),
    GYEO("경기도"),
    GANG("강원도"),
    CHUB("충청북도"),
    CHUN("충청남도"),
    JEOB("전라북도"),
    JEON("전라남도"),
    GYUB("경상북도"),
    GYUN("경상남도"),
    JEJU("제주도")
}

enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다.")
}

enum class ROLE{
    MEMBER
}
