package ayds.lisboa.songinfo.home.model.entities

object DateFormatInjector {

    private lateinit var song: Song

    private lateinit var dateFormat : DateFormat

    fun init(lateSong: Song){
        song=lateSong
        dateFormat = DateFormatImpl(song)
    }

    fun getDateFormat(): DateFormat = dateFormat

}