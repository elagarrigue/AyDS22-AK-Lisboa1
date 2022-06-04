package ayds.newyork2.newyorkdata.nytimes

interface NYTimesService {

    fun getArtist(artistName: String) : NYTimesArtistInfo?
}