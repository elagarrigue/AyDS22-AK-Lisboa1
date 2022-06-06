package ayds.newyork2.newyorkdata.nytimes

import retrofit2.Response

internal class NYTimesServiceImpl (private val artistResolver : NYTimesToArtistResolver, private val apiFromNYTimes : NYTimesAPI):
    NYTimesService {

    override fun getArtist(artistName: String) : NYTimesArtistInfo? {
        val callResponse = getRawArtistInfoFromExternal(artistName)
        return artistResolver.getArtistFromExternalData(callResponse.body(), artistName)
    }

    private fun getRawArtistInfoFromExternal(artistName : String) : Response<String?> {
        return apiFromNYTimes.getArtistInfo(artistName)?.execute() ?: throw Exception("Response not found")
    }
}
