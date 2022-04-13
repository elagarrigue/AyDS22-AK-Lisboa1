package ayds.lisboa.songinfo.home.model.entities

import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.LeapYearCheck

interface DateFormat {
    fun writeReleaseDatePrecision() : String
}

internal class DateFormatImpl(
    private val song: Song
) : DateFormat {

    private val leapYearCheck: LeapYearCheck = UtilsInjector.leapYearCheck

    private val songDate: List<String> = song.releaseDate.split("-")

    override fun writeReleaseDatePrecision(): String = when(song.releaseDatePrecision){
        "day" -> releaseDay()
        "month" -> releaseMonth()
        "year" -> releaseYear()
        else -> releaseEmpty()
    }

    private fun releaseDay(): String {
        val day = songDate.last()
        val month = songDate[1]
        val year = songDate.first()

        return "$day/$month/$year"
    }

    private fun releaseMonth(): String {
        val month = songDate.last()
        val year = songDate.first()

        return Months.values()[month.toInt()-1].toString() + ", " + year
    }

    private fun releaseYear(): String {
        val year = songDate.first()
        val leap = leapYearCheck.isLeapYear(year.toInt())

        return year + if(leap) " (leap year)" else " (not a leap year)"
    }

    private fun releaseEmpty(): String = "Release Date Precision not detected"
}