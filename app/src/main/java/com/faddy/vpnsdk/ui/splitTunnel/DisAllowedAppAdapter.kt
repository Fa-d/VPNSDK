package com.faddy.vpnsdk.ui.splitTunnel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.faddy.vpnsdk.databinding.ItemAppMinusBinding

class DisAllowedAppAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var dataList: MutableList<AppModel> = mutableListOf()
    var addRemovedClicked: ((data: AppModel?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AppViewHolder(
            ItemAppMinusBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AppViewHolder) {
            holder.bind(data = dataList[position])
        }
    }

    inner class AppViewHolder(private val binding: ItemAppMinusBinding) : ViewHolder(binding.root) {

        fun bind(data: AppModel) {

            binding.appIconIv.setImageDrawable(data.appIcon)
            binding.appNameTv.text = data.appName

            /*if (data.isAllowed) {
                binding.addRemoveIconIv.rotation = 45F
            } else {
                binding.addRemoveIconIv.rotation = 0F
            }*/

            binding.addRemoveIconIv.setOnClickListener {
                addRemovedClicked?.invoke(data)
                dataList.remove(data)
                notifyDataSetChanged()
            }

        }

    }

    fun loadApps(dataList: List<AppModel>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)

        notifyItemRangeInserted(0, dataList.size)
    }

    fun filterList(filterlist: List<AppModel>) {
        dataList = filterlist.toMutableList()
        notifyDataSetChanged()
    }

    fun addApp(data: AppModel) {
        dataList.add(data)
        notifyDataSetChanged()
    }

}