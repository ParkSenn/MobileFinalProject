package ddwu.mobilecom.week01.finalproject.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.mobilecom.week01.finalproject.data.MemoDto
import ddwu.mobilecom.week01.finalproject.databinding.ListMemoBinding

class MemoAdapter: RecyclerView.Adapter<MemoAdapter.MemoHolder>(){

    var memoList: List<MemoDto>? = null
    var itemClickListener: OnMemoItemClickListener? = null

    override fun getItemCount(): Int {
        return memoList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        val itemBinding = ListMemoBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return MemoHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val dto = memoList?.get(position)
        holder.itemBinding.tvName.text = dto?.hosName
        holder.itemBinding.tvAddress.text = dto?.hosAddr
        holder.itemBinding.clHos.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    class MemoHolder(val itemBinding: ListMemoBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnMemoItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnMemoItemClickListener) {
        itemClickListener = listener
    }


}