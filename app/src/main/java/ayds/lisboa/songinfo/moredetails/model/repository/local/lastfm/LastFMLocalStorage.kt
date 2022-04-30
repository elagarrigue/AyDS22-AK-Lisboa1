package ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm

interface LastFMLocalStorage {

    fun saveArtist(artist: String?, info: String?)

    fun getInfo(artist: String?): String?
}