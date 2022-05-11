package ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_API = "https://ws.audioscrobbler.com/2.0/"

//TODO aparece como no utilizada
internal class LastFMImpl() {
    fun getLastFM(): LastFMAPI {
        return initRetrofit().create(LastFMAPI::class.java)
    }

   private fun initRetrofit() = Retrofit.Builder()
       .baseUrl(LASTFM_API)
       .addConverterFactory(ScalarsConverterFactory.create())
       .build()
}