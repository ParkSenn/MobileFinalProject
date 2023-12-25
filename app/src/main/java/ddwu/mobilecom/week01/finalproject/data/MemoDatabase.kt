package ddwu.mobilecom.week01.finalproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoDto::class], version = 1, exportSchema = false) // 또는 스키마를 내보내고 싶으면 true로 설정
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao() : MemoDao

    companion object {
        @Volatile       // Main memory 에 저장한 값 사용
        private var INSTANCE : MemoDatabase? = null

        fun getDatabase(context: Context) : MemoDatabase {
            return INSTANCE ?: synchronized(this) {     // 단일 스레드만 접근
                val instance = Room.databaseBuilder(context.applicationContext,
                    MemoDatabase::class.java, "memo_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}