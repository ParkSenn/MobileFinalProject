package ddwu.mobilecom.week01.finalproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.mobilecom.week01.finalproject.data.Hospital
import ddwu.mobilecom.week01.finalproject.data.HospitalRoot
import ddwu.mobilecom.week01.finalproject.databinding.ActivityDetailBinding
import ddwu.mobilecom.week01.finalproject.databinding.ActivityMapBinding
import ddwu.mobilecom.week01.finalproject.network.IHospitalAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapActivity : AppCompatActivity() {
    private lateinit var mapBinding: ActivityMapBinding
    private lateinit var googleMap : GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var currentLoc : Location
    private var marker: Marker? = null
    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(mapBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkPermissions ()

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IHospitalAPIService::class.java)

        val apiKey = resources.getString(R.string.kobis_key)

        val apiCallback = object : Callback<HospitalRoot> {
            override fun onResponse(call: Call<HospitalRoot>, response: Response<HospitalRoot>) {
                if (response.isSuccessful) {
                    // HospitalRoot 객체에서 병원 목록 추출
                    val allHospitals = response.body()?.tbHospitalInfo?.hospitals ?: listOf()
                    addHospitalMarkers(allHospitals)
                } else {
                    Log.d(ContentValues.TAG, "Unsuccessful Response: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<HospitalRoot>, t: Throwable) {
                Log.d(ContentValues.TAG, "OpenAPI Call Failure ${t.message}")
            }
        }

        val apiCall = service.getHospitalInfo(apiKey, 1, 1000)
        apiCall.enqueue(apiCallback)
    }

    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            startLocUpdates() // 위치 업데이트 시작

            googleMap.setOnInfoWindowClickListener { marker ->
                val hospital = marker.tag as? Hospital
                hospital?.let {
                    val intent = Intent(this@MapActivity, DetailActivity::class.java)
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

                    // 필요한 경우 추가 데이터 전달
                    startActivity(intent)
                }

            }

            Log.d(ContentValues.TAG, "GoogleMap is ready")
        }
    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)

    }

    override fun onResume() {
        super.onResume()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocUpdates()
        }
    }

    fun checkPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permissions are already granted", Toast.LENGTH_SHORT).show()
//            startLocUpdates() // 위치 업데이트 시작
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }


    val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Toast.makeText(this, "FINE_LOCATION is granted", Toast.LENGTH_SHORT).show()
                startLocUpdates()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Toast.makeText(this, "COARSE_LOCATION is granted", Toast.LENGTH_SHORT).show()
                startLocUpdates()
            }
            else -> {
                Toast.makeText(this, "Location permissions are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val locCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locResult: LocationResult) {
            currentLoc = locResult.locations[0]
            val newMarkerLoc: LatLng = LatLng(currentLoc.latitude, currentLoc.longitude)

            marker?.remove()

            addMarker(newMarkerLoc)

            if (isFirst) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newMarkerLoc, 17F))
                isFirst= false // 첫 업데이트 완료 후에는 지도 이동 X
            }

            // 지도 카메라 위치 업데이트
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newMarkerLoc, 17F))

//            Toast.makeText(this@MapActivity, "위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}", Toast.LENGTH_SHORT).show()
        }
    }


    val locRequest : LocationRequest = LocationRequest.Builder(10000)
        .setMinUpdateIntervalMillis(15000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    @SuppressLint("MissingPermission")
    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,
            locCallback,
            Looper.getMainLooper()
        )

    }

    fun addMarker(targetLoc: LatLng) {
        marker?.remove()

        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title("내 위치")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        marker = googleMap.addMarker(markerOptions)
    }

    fun addHospitalMarkers(hospitals: List<Hospital>) {
        for (hospital in hospitals) {
            val hospitalLatLng = LatLng(hospital.lat.toDouble(), hospital.lng.toDouble())
            val markerOptions = MarkerOptions()
                .position(hospitalLatLng)
                .title(hospital.name)
                .snippet(hospital.mapimg)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

            val marker = googleMap.addMarker(markerOptions)
            marker?.tag = hospital // 마커에 Hospital 객체 저장
        }
    }

}