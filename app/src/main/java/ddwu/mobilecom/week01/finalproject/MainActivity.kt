package ddwu.mobilecom.week01.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwu.mobilecom.week01.finalproject.databinding.ActivityMainBinding
import ddwu.mobilecom.week01.finalproject.network.IHospitalAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.getMapBtn.setOnClickListener {
            val mapIntent = Intent(this, MapActivity::class.java)
            startActivity(mapIntent)
        }

        binding.getSearchBtn.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.getMemoBtn.setOnClickListener {
            val memoIntent = Intent(this, ShowMemoActivity::class.java)
            startActivity(memoIntent)
        }


    }
}