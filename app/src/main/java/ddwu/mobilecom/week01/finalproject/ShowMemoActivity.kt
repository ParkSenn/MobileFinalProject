package ddwu.mobilecom.week01.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.mobilecom.week01.finalproject.data.MemoDao
import ddwu.mobilecom.week01.finalproject.data.MemoDatabase
import ddwu.mobilecom.week01.finalproject.databinding.ActivityDetailBinding
import ddwu.mobilecom.week01.finalproject.databinding.ActivityShowMemoBinding
import ddwu.mobilecom.week01.finalproject.ui.MemoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 메모 리스트 출력 액티비티
class ShowMemoActivity : AppCompatActivity() {
    private lateinit var showMemoBinding: ActivityShowMemoBinding

    val memoDB : MemoDatabase by lazy {
        MemoDatabase.getDatabase(this)
    }

    val memoDao : MemoDao by lazy {
        memoDB.memoDao()
    }

    val adapter : MemoAdapter by lazy {
        MemoAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showMemoBinding = ActivityShowMemoBinding.inflate(layoutInflater)
        setContentView(showMemoBinding.root)

        showMemoBinding.rvMemo.adapter = adapter
        showMemoBinding.rvMemo.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: MemoAdapter.OnMemoItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent (this@ShowMemoActivity, ShowDetailMemoActivity::class.java )
                intent.putExtra("memoDto", adapter.memoList?.get(position))
                startActivity(intent)
            }
        })

        showAllMemo()
    }

    fun showAllMemo() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                memoDao.getAllMemos().collect { memos ->
                    adapter.memoList = memos
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}