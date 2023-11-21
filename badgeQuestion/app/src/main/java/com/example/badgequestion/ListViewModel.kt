package com.example.badgequestion

import android.accessibilityservice.GestureDescription.StrokeDescription
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File

class ListViewModel:ViewModel() {
    var listData = MutableLiveData<MutableList<EventData>>()
    var badgeCount = MutableLiveData<Int>()
    init {
        badgeCount.value = 10
    }
    suspend fun getData(file: File){
        var dataList = mutableListOf<EventData>()
        var trueCount = 0
        withContext(Dispatchers.Default){
            val jsonStr = file.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val title = jsonObject.getString("eventTitle")
                val status = jsonObject.getBoolean("eventReadStatus")
                val photo = jsonObject.getString("eventPictures")
                dataList.add(EventData(title, status, photo))
            }
            trueCount = dataList.count{it.status}
        }
        withContext(Dispatchers.Main){
            listData.value = dataList
            badgeCount.value = 10 - trueCount
        }
    }
    suspend fun changeStatus(file: File,target: EventData){
        withContext(Dispatchers.Default) {
            val jsonStr = file.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val title = jsonObject.getString("eventTitle")
                if (title == target.title) {
                    jsonObject.put("eventReadStatus", true)
                    break
                }
            }
            file.writeText(jsonArray.toString())
        }
        getData(file)
    }
}
data class EventData(val title: String,val status: Boolean,val photo: String)