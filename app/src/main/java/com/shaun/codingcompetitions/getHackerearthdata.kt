package com.shaun.codingcompetitions

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class getHackerearthData(private val listener: OnDataAvailable_hackerearth) {
    private val TAG = "getHackerearthData"

    interface OnDataAvailable_hackerearth {
        fun OnDataAvailable_hackerearth(data: List<hackerearthData>,id :Int)
        fun onError(exception: Exception)
    }

     fun doInBackground(vararg params: String?): ArrayList<hackerearthData> {
        Log.d(TAG, "doInBackground Starts")
        val contestDetails = ArrayList<hackerearthData>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("objects")
            for (i in 0 until itemsArray.length()) {
                val jsoncontestDetails = itemsArray.getJSONObject(i)
                val contestname = jsoncontestDetails.getString("event")
                val link = jsoncontestDetails.getString("href")

                val start = jsoncontestDetails.getString("start")
                val end = jsoncontestDetails.getString("end")
                val duration = jsoncontestDetails.getInt("duration")

                val contestObject = hackerearthData(contestname, link, start, end, duration)
                contestDetails.add(contestObject)
                Log.d(TAG, "doInBackground $contestObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d(TAG, "doInBackground Error  ${e.message}")
            listener.onError(e)

        }
        Log.d(TAG, "doInBackground Ends")
        return contestDetails
    }

     fun onPostExecute(result: ArrayList<hackerearthData>,id: Int) {
        Log.d(TAG, "onPostExecute stareted")
        listener.OnDataAvailable_hackerearth(result,id )
        Log.d(TAG, "onPostExecute ends")
    }

    fun execute(rawData: String,id: Int){
        GlobalScope.launch {
            var result=doInBackground(rawData)
            withContext(Dispatchers.Main){
                onPostExecute(result,id )
            }
        }

    }



}