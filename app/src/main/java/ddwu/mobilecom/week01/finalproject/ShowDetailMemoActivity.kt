package ddwu.mobilecom.week01.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwu.mobilecom.week01.finalproject.data.MemoDto
import ddwu.mobilecom.week01.finalproject.databinding.ActivityShowDetailMemoBinding
import ddwu.mobilecom.week01.finalproject.databinding.ActivityShowMemoBinding

class ShowDetailMemoActivity : AppCompatActivity() {
    private lateinit var showDetailMemoBinding: ActivityShowDetailMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showDetailMemoBinding = ActivityShowDetailMemoBinding.inflate(layoutInflater)
        setContentView(showDetailMemoBinding.root)

        val memo = intent.getSerializableExtra("memoDto") as MemoDto?
        showDetailMemoBinding.tvHosName.text = memo?.hosName
        showDetailMemoBinding.tvHosAddr.text = memo?.hosAddr
        showDetailMemoBinding.tvMemo.text = memo?.memo
    }
}