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
private const val ARTIST_BIOGRAPHY = "bio"
private const val ARTIST_BIOGRAPHY_EXTRACT = "content"
private const val ARTIST_BIOGRAPHY_URL = "url"
private const val NO_RESULTS = "no result"

internal class JsonToArtistBiographyResolver(): LastFMToArtistBiographyResolver {

    override fun getArtistBiographyFromExternalData(serviceData: String?): LastFMArtistBiography? =
        try {
            //serviceData?.getFirstItem()?.let { item ->
            serviceData?.getItem()?.let { item ->   //TODO asegurarse que serviceData sea el artistName
                LastFMArtistBiography(
                    item.getArtist(), item.getBiography(), item.getExtract(serviceData), item.getUrl()
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        return jobj.asJsonObject
    }

    /*private fun String?.getFirstItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val artists = jobj[ARTIST].asJsonObject
        val items = artists[ITEMS].asJsonArray
        return items[0].asJsonObject
    }
    */

    private fun JsonObject.getArtist(): String {
        val artist = this[ARTIST].asJsonObject
        return artist.asString
    }

    private fun JsonObject.getBiography(): String {
        val bio = this[ARTIST_BIOGRAPHY].asJsonObject
        return bio.asString
    }

    private fun JsonObject.getExtract(artistName: String?): String {
        val bio = this[ARTIST_BIOGRAPHY].asJsonObject
        val extract = bio[ARTIST_BIOGRAPHY_EXTRACT].asString
        val convert : ConvertStringToHTML = UtilsInjector.convertStringToHTML
        return if (extract.isEmpty()) NO_RESULTS else convert.convertTextToHtml(extract,artistName)
    }

    private fun JsonObject.getUrl(): String {
        val artist = this[ARTIST].asJsonObject
        return artist[ARTIST_BIOGRAPHY_URL].asString
    }

}

