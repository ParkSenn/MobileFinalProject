package ddwu.mobilecom.week01.finalproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.mobilecom.week01.finalproject.databinding.ActivityDetailBinding
import ddwu.mobilecom.week01.finalproject.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var googleMap : GoogleMap
    private lateinit var targetLoc : LatLng
    private lateinit var marker : Marker
    private  var name: String? = null
    private  var mapimg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        name = intent.getStringExtra("name")
        val tel = intent.getStringExtra("tel")
        val addr = intent.getStringExtra("addr")
        mapimg = intent.getStringExtra("mapimg")

        val monOpen = intent.getStringExtra("monOpen")
        val tueOpen = intent.getStringExtra("tueOpen")
        val wenOpen = intent.getStringExtra("wenOpen")
        val thurOpen = intent.getStringExtra("thurOpen")
        val friOpen = intent.getStringExtra("friOpen")
        val satOpen = intent.getStringExtra("satOpen")
        val sunOpen = intent.getStringExtra("sunOpen")
        val holOpen = intent.getStringExtra("holOpen")

        val monClose = intent.getStringExtra("monClose")
        val tueClose = intent.getStringExtra("tueClose")
        val wenClose = intent.getStringExtra("wenClose")
        val thurClose = intent.getStringExtra("thurClose")
        val friClose = intent.getStringExtra("friClose")
        val satClose = intent.getStringExtra("satClose")
        val sunClose = intent.getStringExtra("sunClose")
        val holClose = intent.getStringExtra("holClose")

        val lngStr = intent.getStringExtra("lng")
        val latStr = intent.getStringExtra("lat")
        val lng = lngStr?.toDoubleOrNull() ?: 0.0
        val lat = latStr?.toDoubleOrNull() ?: 0.0
        targetLoc = LatLng(lat, lng)

        detailBinding.tvHosName.text = name
        detailBinding.tvHosTel.text = tel
        detailBinding.tvHosAddr.text = "${addr}\n${mapimg}"
        detailBinding.tvHosTime.text = """
            월요일 : ${monOpen} ~ ${monClose}
            화요일 : ${tueOpen} ~ ${tueClose}
            수요일 : ${wenOpen} ~ ${wenClose}
            목요일 : ${thurOpen} ~ ${thurClose}
            금요일 : ${friOpen} ~ ${friClose}
            토요일 : ${satOpen} ~ ${satClose}
            일요일 : ${sunOpen} ~ ${sunClose}
            공휴일 : ${holOpen} ~ ${holClose}
        """.trimIndent()

        detailBinding.memoBtn.setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java).apply {
                putExtra("hospitalName", name) // 'name'은 병원 이름
                putExtra("hospitalTel", tel) // 'tel'은 병원 전화번호
                putExtra("hospitalAddr", "${addr}\n" + "${mapimg}")
            }
            startActivity(intent)
        }

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)
    }

    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))

            addMarker(targetLoc, name, mapimg)

        }
    }

    fun addMarker(targetLoc : LatLng, name : String?, mapimg : String?) {
        val markerOptions : MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title(name ?: "Unknown Name")
            .snippet(mapimg ?: "No Image Available")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        marker = googleMap.addMarker(markerOptions)!!
    }


}