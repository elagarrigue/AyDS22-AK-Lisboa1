package ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm

import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography
import retrofit2.Response

internal class LastFMServiceImpl (
    private val lastFMToArtistBiographyResolver : LastFMToArtistBiographyResolver,
    private val lastFMAPI : LastFMAPI
) : LastFMService {
    override fun getArtistBio(artist: String): LastFMArtistBiography? {
        val callResponse = getArtistBioFromService(artist)
        return lastFMToArtistBiographyResolver.getArtistBiographyFromExternalData(callResponse.body())
    }
    private fun getArtistBioFromService(artist: String): Response<String?> {
        val result = lastFMAPI.getArtistInfo(artist)

        return result?.execute() ?: throw Exception("Api is null")
    }
}