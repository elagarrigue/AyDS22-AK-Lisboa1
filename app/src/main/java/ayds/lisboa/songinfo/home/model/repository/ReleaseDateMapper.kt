package ayds.lisboa.songinfo.home.model.repository

import ayds.lisboa.songinfo.home.model.entities.ReleaseDatePrecision

interface ReleaseDateMapper {
    fun mapReleaseDatePrecision(datePrecision: String): ReleaseDatePrecision
}

internal class ReleaseDateMapperImpl: ReleaseDateMapper {

    override fun mapReleaseDatePrecision(datePrecision: String): ReleaseDatePrecision {
        return when(datePrecision) {
            "day" -> ReleaseDatePrecision.DAY
            "month" -> ReleaseDatePrecision.MONTH
            "year" -> ReleaseDatePrecision.YEAR
            else -> ReleaseDatePrecision.EMPTY
        }
    }

}