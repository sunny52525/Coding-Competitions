package com.shaun.codingcompetitions

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.backdrop_fragment.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private var codechefRawData = ""
private var codeforcesRawData = ""    //for caching
private var hackerearthRawData = ""
private var googleRawData = ""
private var atcoderRawData = ""

class MainActivity : AppCompatActivity(), GetRawData.OndownloadComplete,
    getCodeforcesData.OnDataAvailable, getHackerearthData.OnDataAvailable_hackerearth {
    private var aboutDialog: android.app.AlertDialog? = null
    private val TAG = "MainActivity"

    private val recycleradapter = RecyllerViewAdapter(ArrayList())
    val recyclerHacker = RecyllerViewAdapterHackerearth(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate Called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        configureBackdrop()

        textView.setText("Upcoming Contest (Codeforeces)")
        if (codeforcesRawData.isNotEmpty()) {
            onDownloadComplete(codeforcesRawData, DownloadStatus.OK, 1)
        } else {
            val getRawData = GetRawData(this)
            getRawData.execute("https://codeforces.com/api/contest.list", 1)         //default
        }
        recyler_view.layoutManager = LinearLayoutManager(this)
//        recyler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recyler_view, this,1))
        recyler_view.adapter = recycleradapter


        /////////////////
        backdrop_hackerearth.setOnClickListener {
            textView.setText("Upcoming Contest (Hackerearth)")
            if (hackerearthRawData.isNotEmpty()) {
                onDownloadComplete(hackerearthRawData, DownloadStatus.OK, 2)
            } else {
                val getRawDataHack = GetRawData(this)
                getRawDataHack.execute(
                    "https://clist.by/api/v1/json/contest/?username=shaun_mcafee&api_key=1298d2c77ee2d3f264e61991d6bb63e3cf16361a&limit=100&resource__name=hackerearth.com&filtered=false&order_by=-end",
                    2
                )
            }
            recyler_view.layoutManager = LinearLayoutManager(this)
//            recyler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recyler_view, this,2))
            recyler_view.adapter = recyclerHacker
            onBackPressed()
        }
        backdrop_codeforces.setOnClickListener {
            textView.setText("Upcoming Contest (Codeforeces)")
            Log.d(TAG, "$codeforcesRawData")
            onDownloadComplete(codeforcesRawData, DownloadStatus.OK, 1)
            recyler_view.layoutManager = LinearLayoutManager(this)
//            recyler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recyler_view, this,1))
            recyler_view.adapter = recycleradapter

            onBackPressed()
        }






        Log.d(TAG, "ONcreate ENds")


    }

    override fun onDownloadComplete(data: String, status: DownloadStatus, id: Int) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete Called")

            if (id == 1) {
                codeforcesRawData = data
                val getCodeforcesdata = getCodeforcesData(this)
                getCodeforcesdata.execute(data)
            } else
                if (id == 2) {
                    hackerearthRawData = data
                    Log.d(TAG, "ondownloadcComplete with Hackerearth data")
                    val gethackerearthdata = getHackerearthData(this)
                    gethackerearthdata.execute(data)
                }
        } else {
            Log.d(TAG, "onDownloadCompleted failed with status $status . Error msg is $data")
        }
    }

    override fun OnDataAvailable(data: List<contestDetails>) {
        Log.d(TAG, ".OndataAvailable Datta is $data")

        recycleradapter.loadNewData(data.asReversed())
        Log.d(TAG, ".OndataAvailable ends")
    }



    @SuppressLint("SimpleDateFormat")
    fun getDated(milli: Int): String {
        val date = Date(milli * 1000L)
        val sdf = SimpleDateFormat("yyy MM dd")
        val actualdate = sdf.format(date)
        return actualdate.replace(" ", "") + "T"
    }

    @SuppressLint("InflateParams")
    private fun showAboutDialog() {
        val messgView = layoutInflater.inflate(R.layout.about, null, false)
        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        aboutDialog = builder.setView(messgView).create()
        aboutDialog?.setCanceledOnTouchOutside(true)
        aboutDialog?.show()
    }

    override fun OnDataAvailable_hackerearth(data: List<hackerearthData>) {
        Log.d(TAG, "Data parsed $data")
        recyclerHacker.loadNewData(data)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_theme -> {
                showAboutDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
