package ddwu.mobilecom.week01.finalproject

import android.content.Intent
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

        showDetailMemoBinding.goBackBtn.setOnClickListener {
            finish()
        }

        showDetailMemoBinding.emailBtn.setOnClickListener {
            shareByEmail(memo)
        }
    }

    private fun shareByEmail(memo: MemoDto?) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_SUBJECT, "${memo?.hosName} 메모 공유합니다")
            putExtra(Intent.EXTRA_TEXT, "병원 이름: ${memo?.hosName}\n" +
                    "주소: ${memo?.hosAddr}\n" +
                    "메모: ${memo?.memo}")
        }
        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }
}