package com.shaun.codingcompetitions

import java.text.SimpleDateFormat
import java.util.*

class contestDetails(
    val id: Int,
    val name: String,
    val TimeDate: Int,
    val phase: String,
    val Duration: Int
) {

    val actualTime = dateIs(TimeDate)

    val time = gethour(Duration)
    val totaltime =
        time[0].toString() + " Days:" + time[1].toString() + " Hours:" + time[2].toString() + " Minutes"

    override fun toString(): String {
        return "ContestDetails(id= $id , Name = $name , TimeData=$actualTime, Duration ={ ${time[0]} : ${time[1]} : ${time[2]}  }, Phase =$phase)"
    }

    fun dateIs(milli: Int): String {
        val date = Date(milli * 1000L)
        val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z")
        sdf.timeZone = TimeZone.getTimeZone("GMT+5:30")
        return sdf.format(date)

    }

    fun gethour(seconds: Int): Array<Int> {
        var time = Array(3) { 0 }
        var dump = seconds
        time[0] = dump / (24 * 3600)
        dump %= (24 * 3600)
        time[1] = dump / 3600
        dump %= 3600
        time[2] = dump / 60

        return time
    }
}