package ddwu.mobilecom.week01.finalproject

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ddwu.mobilecom.week01.finalproject.data.MemoDao
import ddwu.mobilecom.week01.finalproject.data.MemoDatabase
import ddwu.mobilecom.week01.finalproject.data.MemoDto
import ddwu.mobilecom.week01.finalproject.databinding.ActivityDetailBinding
import ddwu.mobilecom.week01.finalproject.databinding.ActivityMemoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoActivity : AppCompatActivity() {

    val memoDB : MemoDatabase by lazy {
        MemoDatabase.getDatabase(this)
    }

    val memoDao : MemoDao by lazy {
        memoDB.memoDao()
    }

    private lateinit var memoBinding: ActivityMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memoBinding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(memoBinding.root)

        val name = intent.getStringExtra("hospitalName") ?: "이름 없음"
        val tel = intent.getStringExtra("hospitalTel") ?: "전화번호 없음"
        val addr = intent.getStringExtra("hospitalAddr") ?: "주소 없음"

        // 받아온 데이터를 뷰에 설정
        memoBinding.tvHosName.text = name
        memoBinding.tvHosTel.text = tel
        memoBinding.tvHosAddr.text = addr

        memoBinding.saveBtn.setOnClickListener {
            val memo = memoBinding.mtvMemo.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    memoDao.insertMemo(MemoDto(0, name, addr, memo))

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MemoActivity, "메모가 작성되었습니다!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error inserting memo", e)
                }
            }
        }



    }
}