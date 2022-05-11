package ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm

import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.ConvertStringToHTML
import com.google.gson.JsonObject
import com.google.gson.Gson

interface LastFMToArtistBiographyResolver {
    fun getArtistBiographyFromExternalData(serviceData:String?): LastFMArtistBiography?
}

private const val ARTIST = "artist"
private const val ARTIST_NAME = "name"
private const val ARTIST_BIOGRAPHY = "bio"
private const val ARTIST_BIOGRAPHY_EXTRACT = "content"
private const val ARTIST_BIOGRAPHY_URL = "url"
private const val NO_RESULTS = "No results"

internal class JsonToArtistBiographyResolver(): LastFMToArtistBiographyResolver {

    override fun getArtistBiographyFromExternalData(serviceData: String?): LastFMArtistBiography? =
        try {
            serviceData?.getItem()?.let { item ->
                LastFMArtistBiography(
                    item.getArtist(), item.getBiography(),  item.getUrl()
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        return jobj[ARTIST].asJsonObject
    }


    private fun JsonObject.getArtist(): String {
        return this[ARTIST_NAME].asString
    }

    private fun JsonObject.getBiography(): String {
        val bio = this[ARTIST_BIOGRAPHY].asJsonObject
        val extract = bio[ARTIST_BIOGRAPHY_EXTRACT].asString
        val convert : ConvertStringToHTML = UtilsInjector.convertStringToHTML
        val artistName = this[ARTIST_NAME].asString
        return if (extract.isEmpty()) NO_RESULTS else convert.convertTextToHtml(extract,artistName)
    }

    private fun JsonObject.getUrl(): String {
        return this[ARTIST_BIOGRAPHY_URL].asString
    }

}

