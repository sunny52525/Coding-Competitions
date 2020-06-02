package com.shaun.codingcompetitions

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.backdrop_fragment.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), GetRawData.OndownloadComplete,
    getCodeforcesData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private var aboutDialog: android.app.AlertDialog?= null
    private val TAG = "MainActivity"
    private val recycleradapter = RecyllerViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate Called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        fab.setOnClickListener {
//           showAboutDialog()
//        }

        sheet_test.setOnClickListener{
            Toast.makeText(this,"well fuck",Toast.LENGTH_LONG).show()
            onBackPressed()
        }
        configureBackdrop()

        val getRawData = GetRawData(this)
//        getRawData.setDownloadCompleteListener(this)

        getRawData.execute("https://codeforces.com/api/contest.list")
//
        recyler_view.layoutManager = LinearLayoutManager(this)
        recyler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recyler_view, this))
        recyler_view.adapter = recycleradapter
        Log.d(TAG, "ONcreate ENds")
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete Called")
            val getFlickerJsonData = getCodeforcesData(this)
            getFlickerJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadCompleted failed with status $status . Error msg is $data")
        }
    }

    override fun OnDataAvailable(data: List<contestDetails>) {
        Log.d(TAG, ".OndataAvailable Datta is $data")

        recycleradapter.loadNewData(data.asReversed())
        Log.d(TAG, ".OndataAvailable ends")
    }


    override fun onItemClick(view: View, postion: Int) {
        Log.d(TAG, "onItemClick Starts")
        val contest = recycleradapter.getContest(postion)
        if (contest != null) {
            var title = contest.name

            val re = Regex("[^A-Za-z0-9 ]")
            title = re.replace(title, "+")
            Log.d(TAG, "Title is $title")
            val dated = getDated(contest.TimeDate)
            var spacecount = 0
            var time = ""
            for (chars in contest.actualTime) {
                if (chars == ' ')
                    spacecount++
                if (spacecount < 4)
                    continue
                if (spacecount > 4)
                    break
                if (chars != ':')
                    time += chars

            }
            var end: String
            var endInt = 0
            for (i in 1..6) {
                endInt += (Math.pow(
                    10.toDouble(),
                    (6 - i).toDouble()
                )).toInt() * (time[i].toInt() - 48)
                println("${time[i].toInt() - 48} ,${time[i]} ,$i")

            }

            println("endInt is $endInt")
            endInt += 20000
            end = endInt.toString()
            end = dated + end + "IST"

            time += "IST"
            time = dated + time
            time = time.replace(" ", "")
            Log.d(TAG, "time is $time")
            Log.d(TAG, "end time is $end")
            Log.d(TAG, "Parsed Date is $dated")


            val URL = "https://www.google.com/calendar/render?action" +
                    "=TEMPLATE&text=$title" +
                    "&" +
                    "dates=$time/$end&details=" +
                    "http://www.codeforces.com/contests&location=India&" +
                    "sf=true&output=xml"
            Log.d(TAG,URL)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL)))
        }

    }

    override fun onItemLongClick(view: View, postion: Int) {
        Log.d(TAG, "onItemLongClick Starts")

    }


    @SuppressLint("SimpleDateFormat")
    fun getDated(milli: Int): String {
        val date = Date(milli * 1000L)
        val sdf = SimpleDateFormat("yyy MM dd")
        val actualdate = sdf.format(date)
        return actualdate.replace(" ", "") + "T"
    }
    @SuppressLint("InflateParams")
    private fun showAboutDialog(){
        val messgView = layoutInflater.inflate(R.layout.about,null,false)
        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        aboutDialog=builder.setView(messgView).create()
        aboutDialog?.setCanceledOnTouchOutside(true)
        aboutDialog?.show()
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "on error wth $exception")
    }

    ////////////////////////////////////////////

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private fun configureBackdrop() {
        // Get the fragment reference
        val fragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)

        fragment?.let {
            // Get the BottomSheetBehavior from the fragment view
            BottomSheetBehavior.from(fragment.requireView())?.let { bsb ->
                // Set the initial state of the BottomSheetBehavior to HIDDEN
                bsb.state = BottomSheetBehavior.STATE_HIDDEN

                // Set the trigger that will expand your view
                fab.setOnClickListener { bsb.state = BottomSheetBehavior.STATE_EXPANDED }

                // Set the reference into class attribute (will be used latter)
                mBottomSheetBehavior = bsb
            }
        }
    }

    override fun onBackPressed() {
        // With the reference of the BottomSheetBehavior stored
        mBottomSheetBehavior?.let {
            if (it.state == BottomSheetBehavior.STATE_EXPANDED) {
                it.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

}
