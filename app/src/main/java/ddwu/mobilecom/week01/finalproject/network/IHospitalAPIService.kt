package ddwu.mobilecom.week01.finalproject.network

import ddwu.mobilecom.week01.finalproject.data.HospitalRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IHospitalAPIService {
    @GET("{apiKey}/json/TbHospitalInfo/{startIndex}/{endIndex}")
    fun getHospitalInfo(
        @Path("apiKey") apiKey: String,
        @Path("startIndex") startIndex: Int,
        @Path("endIndex") endIndex: Int
    ): Call<HospitalRoot>
}