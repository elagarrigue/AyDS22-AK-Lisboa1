package ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object LastFMArtistInjector {

    private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
    private val lastFMAPIRetrofit = Retrofit.Builder()
        .baseUrl(LASTFM_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val lastFMArtistAPI = lastFMAPIRetrofit.create(LastFMAPI::class.java)
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver = JsonToArtistBiographyResolver()

    val lastFMArtistService: LastFMService = LastFMServiceImpl(
        lastFMToArtistBiographyResolver,
        lastFMArtistAPI,
    )
}