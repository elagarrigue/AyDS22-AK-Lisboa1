package ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm

import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography
import com.google.gson.JsonObject
import com.google.gson.Gson

interface LastFMToSongResolver {
    fun getArtistBiographyFromExternalData(serviceData:String?): LastFMArtistBiography?
}

private const val ARTIST = "artist"
private const val ARTIST_BIOGRAPHY = "bio"
private const val ARTIST_BIOGRAPHY_EXTRACT = "content"
private const val ARTIST_BIOGRAPHY_URL = "url"

internal class JsonToSongResolver(): LastFMToSongResolver {

    override fun getArtistBiographyFromExternalData(serviceData: String?): LastFMArtistBiography? =
        try {
            //serviceData?.getFirstItem()?.let { item ->
            serviceData?.getItem()?.let { item ->
                LastFMArtistBiography(
                    item.getArtist(), item.getBiography(), item.getExtract(), item.getUrl()
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

    private fun JsonObject.getExtract(): String {
        val bio = this[ARTIST_BIOGRAPHY].asJsonObject
        return bio[ARTIST_BIOGRAPHY_EXTRACT].asString
    }

    private fun JsonObject.getUrl(): String {
        val artist = this[ARTIST].asJsonObject
        return artist[ARTIST_BIOGRAPHY_URL].asString
    }
}

