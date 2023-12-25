package ddwu.mobilecom.week01.finalproject.data

import com.google.gson.annotations.SerializedName

data class HospitalRoot(
    @SerializedName("TbHospitalInfo")
    val tbHospitalInfo: TbHospitalInfo,
)

data class TbHospitalInfo(
//    @SerializedName("list_total_count")
//    val listTotalCount: Long,
//    @SerializedName("RESULT")
//    val result: Result,
    @SerializedName("row")
    val hospitals: List<Hospital>, // Hospital Info
)

//data class Result(
//    @SerializedName("CODE")
//    val code: String,
//    @SerializedName("MESSAGE")
//    val message: String,
//)

data class Hospital(
    // 주소
    @SerializedName("DUTYADDR")
    val addr: String,
    // 비고 (상세설명보단 짧으나 대체로 점심시간)
//    @SerializedName("DUTYETC")
//    val etc: String,
//    // 기관설명상세
//    @SerializedName("DUTYINF")
//    val info: String,
    // 간이약도
    @SerializedName("DUTYMAPIMG")
    val mapimg: String,
    // 기관명
    @SerializedName("DUTYNAME")
    val name: String,
    // 대표전화1
    @SerializedName("DUTYTEL1")
    val tel: String,
    // 진료 마감 시간 (월~일, 공휴일 마감 시간)
    @SerializedName("DUTYTIME1C")
    val closeTimeMon: String,
    @SerializedName("DUTYTIME2C")
    val closeTimeTue: String,
    @SerializedName("DUTYTIME3C")
    val closeTimeWen: String,
    @SerializedName("DUTYTIME4C")
    val closeTimeThur: String,
    @SerializedName("DUTYTIME5C")
    val closeTimeFri: String,
    @SerializedName("DUTYTIME6C")
    val closeTimeSat: String,
    @SerializedName("DUTYTIME7C")
    val closeTimeSun: String,
    @SerializedName("DUTYTIME8C")
    val closeTimeHol: String,
    // 진료 시작 시간 (월~일, 공휴일 시작 시간)
    @SerializedName("DUTYTIME1S")
    val startTimeMon: String,
    @SerializedName("DUTYTIME2S")
    val startTimeTue: String,
    @SerializedName("DUTYTIME3S")
    val startTimeWen: String,
    @SerializedName("DUTYTIME4S")
    val startTimeThur: String,
    @SerializedName("DUTYTIME5S")
    val startTimeFri: String,
    @SerializedName("DUTYTIME6S")
    val startTimeSat: String,
    @SerializedName("DUTYTIME7S")
    val startTimeSun: String,
    @SerializedName("DUTYTIME8S")
    val startTimeHol: String,
    // 병원 경도
    @SerializedName("WGS84LON")
    val lng: String,
    // 병원 위도
    @SerializedName("WGS84LAT")
    val lat: String,
)

