package com.example.badgequestion

import android.app.LauncherActivity.ListItem
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.example.badgequestion.databinding.FragmentListBinding
import com.example.badgequestion.databinding.ListItemBinding

class ListAdapter(private val dataList: MutableList<EventData>,private val context: Context):RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private var listClicker: ListClicker? = null
    interface ListClicker{
        fun onClick(data: EventData)
    }
    fun setOnListClicker(listener: ListClicker){
        listClicker = listener
    }
    inner class ListViewHolder(private val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(data: EventData){
            val bitmap = getBitmap(data.photo)
            binding.liImage.setImageBitmap(bitmap)
            binding.liTitle.text = data.title
            val status = if(data.status) "Read" else "Unread"
            binding.liStatus.text = status
            binding.root.isSelected = true
            binding.root.setOnClickListener {
                listClicker?.onClick(data)
            }
        }
    }
    private fun getBitmap(name: String):Bitmap{
        val file = context.assets.open(name)
        return BitmapFactory.decodeStream(file)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }
}