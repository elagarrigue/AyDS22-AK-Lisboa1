package ayds.newyork2.newyorkdata.external.nytimes

interface NYTimesService {

    fun getArtist(artistName: String) : ExternalArtistInfo?
}