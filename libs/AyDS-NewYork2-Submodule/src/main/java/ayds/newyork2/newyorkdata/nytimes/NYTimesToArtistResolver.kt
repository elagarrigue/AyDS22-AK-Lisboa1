package ayds.newyork2.newyorkdata.nytimes

interface NYTimesToArtistResolver {

    fun getArtistFromExternalData(serviceData: String?, artistName: String) : NYTimesArtistInfo?
}