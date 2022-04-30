package ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm

import ayds.lisboa.songinfo.moredetails.LASTFM_API
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object LastFMArtistInjector {

    private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
    private val lastFMAPIRetrofit = Retrofit.Builder()
        .baseUrl(LASTFM_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val lastFMArtistAPI = lastFMAPIRetrofit.create(LastFMArtistAPI::class.java)
    private val lastFMToSongResolver: LastFMToSongResolver = JsonToSongResolver()

    val lastFMArtistService: LastFMArtistService = LastFMArtistServiceImpl(
        lastFMArtistAPI,
        lastFMAuthInjector.lastFMAccountService,
        lastFMToSongResolver
    )
}