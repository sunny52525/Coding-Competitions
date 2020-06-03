package com.shaun.codingcompetitions

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class getCodeforcesData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<contestDetails>>() {
    private val TAG = "getCodeforcesData"

    interface OnDataAvailable {
        fun OnDataAvailable(data: List<contestDetails>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): ArrayList<contestDetails> {
        Log.d(TAG, "doInBackground Starts")
        val contestDetails = ArrayList<contestDetails>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("result")
            for (i in 0 until itemsArray.length()) {
                val jsoncontestDetails = itemsArray.getJSONObject(i)
                val id = jsoncontestDetails.getInt("id")
                val name = jsoncontestDetails.getString("name")
                if (jsoncontestDetails.getString("phase") == "FINISHED")
                    break
                val phase = jsoncontestDetails.getString("phase")
                val timedate = jsoncontestDetails.getInt("startTimeSeconds")
                val duration = jsoncontestDetails.getInt("durationSeconds")

                val contestObject = contestDetails(id, name, timedate, phase, duration)
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

    override fun onPostExecute(result: ArrayList<contestDetails>) {
        Log.d(TAG, "onPostExecute stareted")
        super.onPostExecute(result)
        listener.OnDataAvailable(result)
        Log.d(TAG, "onPostExecute ends")
    }
}