package com.shaun.codingcompetitions

class hackerearthData(
    val contestName: String,
    val link: String,
    val start: String,
    val end: String,
    var duration: Int
) {

    val starttime=start.replace('T',' ')
    val endtime=end.replace('T',' ')
    val durationinHour = gethour(duration)
    val totaltime =
        durationinHour[0].toString() + " Days:" + durationinHour[1].toString() + " Hours:" + durationinHour[2].toString() + " Minutes"

    override fun toString(): String {
        return "contest Name $contestName , link is $link , start time $start , end time $end , duration is $duration"
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

