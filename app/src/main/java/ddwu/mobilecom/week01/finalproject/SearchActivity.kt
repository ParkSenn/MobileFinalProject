package ddwu.mobilecom.week01.finalproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.mobilecom.week01.finalproject.data.Hospital
import ddwu.mobilecom.week01.finalproject.data.HospitalRoot
import ddwu.mobilecom.week01.finalproject.databinding.ActivityMainBinding
import ddwu.mobilecom.week01.finalproject.databinding.ActivitySearchBinding
import ddwu.mobilecom.week01.finalproject.network.IHospitalAPIService
import ddwu.mobilecom.week01.finalproject.ui.HospitalAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import retrofit2.http.Query

class SearchActivity : AppCompatActivity() {
    private lateinit var searchBinding: ActivitySearchBinding
    lateinit var adapter : HospitalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        val adapter = HospitalAdapter { hospital ->

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", hospital.name)
            intent.putExtra("tel", hospital.tel)
            intent.putExtra("addr", hospital.addr)
            intent.putExtra("mapimg", hospital.mapimg)

            intent.putExtra("monOpen", hospital.startTimeMon)
            intent.putExtra("tueOpen", hospital.startTimeTue)
            intent.putExtra("wenOpen", hospital.startTimeWen)
            intent.putExtra("thurOpen", hospital.startTimeThur)
            intent.putExtra("friOpen", hospital.startTimeFri)
            intent.putExtra("satOpen", hospital.startTimeSat)
            intent.putExtra("sunOpen", hospital.startTimeSun)
            intent.putExtra("holOpen", hospital.startTimeHol)

            intent.putExtra("monClose", hospital.closeTimeMon)
            intent.putExtra("tueClose", hospital.closeTimeTue)
            intent.putExtra("wenClose", hospital.closeTimeWen)
            intent.putExtra("thurClose", hospital.closeTimeThur)
            intent.putExtra("friClose", hospital.closeTimeFri)
            intent.putExtra("satClose", hospital.closeTimeSat)
            intent.putExtra("sunClose", hospital.closeTimeSun)
            intent.putExtra("holClose", hospital.closeTimeHol)

            intent.putExtra("lng", hospital.lng)
            intent.putExtra("lat", hospital.lat)

            startActivity(intent)
        }
        searchBinding.rvHospitals.adapter = adapter
        searchBinding.rvHospitals.layoutManager = LinearLayoutManager(this)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(IHospitalAPIService::class.java)

        searchBinding.btnSearch.setOnClickListener {
            val searchQuery = searchBinding.etSearch.text.toString()
            val apiKey = resources.getString(R.string.kobis_key)

            val apiCallback = object : Callback<HospitalRoot> {
                override fun onResponse(call: Call<HospitalRoot>, response: Response<HospitalRoot>) {
                    if (response.isSuccessful) {
                        val allHospitals = response.body()?.tbHospitalInfo?.hospitals ?: listOf()
                        val filteredHospitals = filterHospitals(allHospitals, searchQuery)
                        if (filteredHospitals.isNotEmpty()) {
                            Log.d(TAG, "Filtered Hospitals Found: ${filteredHospitals.size}")

                            adapter.hospitals = filteredHospitals
                            adapter.notifyDataSetChanged()
                        } else {
                            Log.d(TAG, "No Hospitals Found Matching Query: $searchQuery")
                        }
                    } else {
                        Log.d(TAG, "Unsuccessful Response: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<HospitalRoot>, t: Throwable) {
                    Log.d(TAG, "OpenAPI Call Failure ${t.message}")
                }
            }

            val apiCall = service.getHospitalInfo(apiKey, 1, 1000)
            apiCall.enqueue(apiCallback)
        }


    }

    private fun filterHospitals(hospitals: List<Hospital>, query: String): List<Hospital> {
        return hospitals.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.addr.contains(query, ignoreCase = true) ||
                    it.mapimg.contains(query, ignoreCase = true)
        }
    }
}