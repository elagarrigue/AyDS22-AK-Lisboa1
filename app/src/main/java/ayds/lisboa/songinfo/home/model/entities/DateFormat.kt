package ayds.lisboa.songinfo.home.model.entities

import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.LeapYearCheck

interface DateFormat {
    fun writeReleaseDatePrecision(song: Song) : String
}

internal class DateFormatImpl() : DateFormat {

    private val leapYearCheck: LeapYearCheck = UtilsInjector.leapYearCheck

    override fun writeReleaseDatePrecision(song: Song): String = when(song.releaseDatePrecision){
            "day" -> releaseDay(song.releaseDate)
            "month" -> releaseMonth(song.releaseDate)
            else -> releaseYear(song.releaseDate)
        }

    private fun releaseDay(releaseDate : String): String = releaseDate.split("-").last() + "/" + releaseDate.split("-")[1] + "/" + releaseDate.split("-").first()

    private fun releaseMonth(releaseDate : String): String = MONTHS.values()[releaseDate.split("-").last().toInt()-1].toString() + ", " + releaseDate.split("-").first()

    private fun releaseYear(releaseDate : String): String = releaseDate.split("-").first() + if(leapYearCheck.isLeapYear(releaseDate.split("-").first().toInt())) " (leap year)" else " (not a leap year)"


}