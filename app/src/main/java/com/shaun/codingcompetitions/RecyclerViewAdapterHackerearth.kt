package com.shaun.codingcompetitions

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HackerearthAdapter(view: View) : RecyclerView.ViewHolder(view) {
    var contestName: TextView = view.findViewById(R.id.hackerearth_name)
    var start: TextView = view.findViewById(R.id.hackearth_starts)
    var ends: TextView = view.findViewById(R.id.hackerearth_ends)
    var duration: TextView = view.findViewById(R.id.hackearth_duration)
    var imageIcon: ImageView = view.findViewById(R.id.imageView)

}


class RecyllerViewAdapterHackerearth(private var contests: List<hackerearthData>) :
    RecyclerView.Adapter<HackerearthAdapter>() {
    private val TAG = "RecyclerViewAdapt"
    var idThis = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackerearthAdapter {
        Log.d(TAG, "onCreateViewHolder new view requested")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.hackerearth_adapter, parent, false)
        return HackerearthAdapter(view)
    }

    override fun getItemCount(): Int {
//        Log.d(TAG,".getItemCount called")
        return if (contests.isNotEmpty()) contests.size else 0
    }

    fun loadNewData(newContest: List<hackerearthData>, id: Int) {
        contests = newContest
        idThis = id
        notifyDataSetChanged()
    }

    fun getContest(position: Int): hackerearthData? {
        return if (contests.isNotEmpty()) contests[position] else null
    }


    override fun onBindViewHolder(holder: HackerearthAdapter, position: Int) {
        //called by layout manager (needs existing view)

        if (contests.isEmpty()) {
            holder.contestName.setText("NO UPCOMING CONTEST FOUND")
            holder.start.setText("")
            holder.ends.setText("")
            holder.duration.setText("")
        } else {

            val contestItem = contests[position]
//        Log.d(TAG,".onBindViewHolder ${photoItem.title} -> $position")
            holder.contestName.text = contestItem.contestName
            holder.start.text = contestItem.starttime
            holder.ends.text = contestItem.endtime
            holder.duration.setText(contestItem.totaltime.toString())

            if (idThis == 2) {
                holder.imageIcon.setImageResource(R.drawable.hackereath)
            } else if (idThis == 3) {
                holder.imageIcon.setImageResource(R.drawable.atcoder)
            } else if (idThis == 4) {
                holder.imageIcon.setImageResource(R.drawable.google)
            }
        }


    }

}