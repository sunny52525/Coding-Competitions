package com.shaun.codingcompetitions


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Codeforcescontestadapter(view: View) : RecyclerView.ViewHolder(view) {
    var contestName: TextView = view.findViewById(R.id.contest_name)
    var date: TextView = view.findViewById(R.id.date)
    var contestid: TextView = view.findViewById(R.id.contestId)
    var duration: TextView = view.findViewById(R.id.duration)


}


class RecyllerViewAdapter(private var contests: List<contestDetails>) :
    RecyclerView.Adapter<Codeforcescontestadapter>() {
    private val TAG = "RecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Codeforcescontestadapter {
        Log.d(TAG, "onCreateViewHolder new view requested")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.competitionslists, parent, false)
        return Codeforcescontestadapter(view)
    }

    override fun getItemCount(): Int {
//        Log.d(TAG,".getItemCount called")
        return if (contests.isNotEmpty()) contests.size else 0
    }

    fun loadNewData(newContest: List<contestDetails>) {
        contests = newContest
        notifyDataSetChanged()
    }

    fun getContest(position: Int): contestDetails? {
        return if (contests.isNotEmpty()) contests[position] else null
    }


    override fun onBindViewHolder(holder: Codeforcescontestadapter, position: Int) {
        //called by layout manager (needs existing view)

        if (contests.isEmpty()) {
            holder.contestName.setText("NO UPCOMING CONTEST FOUND")
            holder.date.setText("")
            holder.contestid.setText("")
            holder.duration.setText("")
        } else {

            val contestItem = contests[position]
//        Log.d(TAG,".onBindViewHolder ${photoItem.title} -> $position")
            holder.contestName.text = contestItem.name
            holder.date.text = contestItem.actualTime
            holder.contestid.text = "#" + contestItem.id.toString()
            holder.duration.text = contestItem.totaltime

        }


    }
}