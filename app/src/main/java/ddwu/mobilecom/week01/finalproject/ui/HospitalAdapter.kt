package ddwu.mobilecom.week01.finalproject.ui

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.mobilecom.week01.finalproject.data.Hospital
import ddwu.mobilecom.week01.finalproject.databinding.ListItemBinding



class HospitalAdapter(private val onClick: (Hospital) -> Unit) : RecyclerView.Adapter<HospitalAdapter.HospitalHolder>() {
    var hospitals: List<Hospital>? = null

    override fun getItemCount(): Int {
        return hospitals?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HospitalHolder, position: Int) {
        val hospital = hospitals?.get(position)
        hospital?.let {
            holder.itemBinding.tvName.text = it.name  // 병원 이름
            holder.itemBinding.tvAddress.text = it.addr  // 병원 주소
            holder.itemBinding.tvPhone.text = it.tel  // 병원 전화번호

            holder.itemBinding.root.layoutParams = holder.itemBinding.root.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            holder.itemView.setOnClickListener {
                onClick(hospital)
            }

            Log.d(TAG, "Item at position $position - Height: ${holder.itemBinding.root.layoutParams.height}")
        }
    }

    class HospitalHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}
