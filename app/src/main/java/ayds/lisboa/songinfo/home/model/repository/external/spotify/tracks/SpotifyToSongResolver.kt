package ayds.lisboa.songinfo.home.model.repository.external.spotify.tracks

import ayds.lisboa.songinfo.home.model.repository.ReleaseDateMapper
import ayds.lisboa.songinfo.home.model.entities.ReleaseDatePrecision
import com.google.gson.Gson
import ayds.lisboa.songinfo.home.model.entities.SpotifySong
import com.google.gson.JsonObject

interface SpotifyToSongResolver {
    fun getSongFromExternalData(serviceData: String?): SpotifySong?
}

private const val TRACKS = "tracks"
private const val ITEMS = "items"
private const val ID = "id"
private const val NAME = "name"
private const val ARTISTS = "artists"
private const val ALBUM = "album"
private const val IMAGES = "images"
private const val RELEASE_DATE = "release_date"
private const val URL = "url"
private const val EXTERNAL_URL = "external_urls"
private const val SPOTIFY = "spotify"
private const val RELEASE_DATE_PRECISION = "release_date_precision"

internal class JsonToSongResolver(private val releaseDateMapper: ReleaseDateMapper) : SpotifyToSongResolver {

    override fun getSongFromExternalData(serviceData: String?): SpotifySong? =
        try {
            println(serviceData);
            serviceData?.getFirstItem()?.let { item ->
                SpotifySong(
                  item.getId(), item.getSongName(), item.getArtistName(), item.getAlbumName(),
                  item.getReleaseDate(), item.getSpotifyUrl(), item.getImageUrl(), item.getReleaseDatePrecision()
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getFirstItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val tracks = jobj[TRACKS].asJsonObject
        val items = tracks[ITEMS].asJsonArray
        return items[0].asJsonObject
    }

    private fun JsonObject.getId() = this[ID].asString

    private fun JsonObject.getSongName() = this[NAME].asString

    private fun JsonObject.getArtistName(): String {
        val artist = this[ARTISTS].asJsonArray[0].asJsonObject
        return artist[NAME].asString
    }

    private fun JsonObject.getAlbumName(): String {
        val album = this[ALBUM].asJsonObject
        return album[NAME].asString
    }

    private fun JsonObject.getReleaseDate(): String {
        val album = this[ALBUM].asJsonObject
        return album[RELEASE_DATE].asString
    }

    private fun JsonObject.getImageUrl(): String {
        val album = this[ALBUM].asJsonObject
        return album[IMAGES].asJsonArray[1].asJsonObject[URL].asString
    }

    private fun JsonObject.getSpotifyUrl(): String {
        val externalUrl = this[EXTERNAL_URL].asJsonObject
        return externalUrl[SPOTIFY].asString
    }

    private fun JsonObject.getReleaseDatePrecision(): ReleaseDatePrecision {
        val album = this[ALBUM].asJsonObject
        val releaseDatePrecision = album[RELEASE_DATE_PRECISION].asString
        return releaseDateMapper.mapReleaseDatePrecision(releaseDatePrecision)
    }

}