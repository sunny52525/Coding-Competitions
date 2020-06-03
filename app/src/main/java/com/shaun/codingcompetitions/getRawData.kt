package com.shaun.codingcompetitions

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}

class GetRawData(private val listener: OndownloadComplete) {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OndownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus, id: Int)
    }

    fun onPostExecute(result: String, id: Int) {
        Log.d(TAG, "onPOst Ex with val $result")
        listener?.onDownloadComplete(result, downloadStatus, id)
    }

    fun doInBackground(vararg params: String?): String {
        Log.d(TAG, "DoInBackground Starts with link ${params[0]}")
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "no URL specified"
        }
        try {

            downloadStatus = DownloadStatus.OK
            println("trying")
            val Data = URL(params[0]).readText()
            Log.d(TAG, Data)
            return Data
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground:Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY

                    "doInBackground:IO Exception ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "doInBackground:Security Exception : Give the Goddamn permission ${e.message}"
                }
                else
                -> {

                    downloadStatus = DownloadStatus.ERROR
                    "Unkown Error : ${e.message}"

                }
            }
            Log.e(TAG, errorMessage)
            return errorMessage
        }
    }

    fun execute(link: String, id: Int) {
        GlobalScope.launch {

            val result = doInBackground(link)

            withContext(Dispatchers.Main) {
                onPostExecute(result, id)
            }
        }
    }
//
//    private fun downloadString(link:String?):String{
//        val result=StringBuilder()
//        try {
//            val url=URL(link)
//            val connection:HttpURLConnection=url.openConnection() as HttpURLConnection
//            val response =connection.responseCode
//            Log.d(TAG,"Response code was $response")
////            val inputStream=connection.inputStream
////            val inoutStreamReader=InputStreamReader(inputStream)
////           val reader=BufferedReader(inoutStreamReader)
//            val reader =BufferedReader(InputStreamReader(connection.inputStream))
//            val inputBuffer =CharArray( 500)
//            var charsRead=0
//            while (charsRead<=0){
//                charsRead=reader.read(inputBuffer)
//                if(charsRead>0){
//                    result.append(String(inputBuffer,0,charsRead))
//                }
//
//            }
//            reader.close()
//            Log.d(TAG,"recived ${result.length}")
//            return result.toString()
//        }catch (e: MalformedURLException){
//            Log.d(TAG,"FUCK HO gya ${e.message}")
//        }catch (e:IOException){
//            Log.d(TAG,"IO EXCEPTION ${e.message}")
//        }catch (e:java.lang.Exception){
//            Log.d(TAG,"Unkown error")
//        }
//
//        return ""
//    }
}