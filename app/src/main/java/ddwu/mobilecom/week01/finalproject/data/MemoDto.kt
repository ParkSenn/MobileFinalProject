package ddwu.mobilecom.week01.finalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "memo_table")
data class MemoDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var hosName: String,
    var hosAddr: String,
    var memo: String) : Serializable {
    override fun toString(): String {
        return "${id} : (${hosName})\n${hosAddr}\n $memo"
    }
}

