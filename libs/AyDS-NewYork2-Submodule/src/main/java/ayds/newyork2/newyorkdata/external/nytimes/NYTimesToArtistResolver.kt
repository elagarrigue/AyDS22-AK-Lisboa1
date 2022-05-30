package ayds.newyork2.newyorkdata.external.nytimes

interface NYTimesToArtistResolver {

    fun getArtistFromExternalData(serviceData: String?, artistName: String) : ExternalArtistInfo?
}