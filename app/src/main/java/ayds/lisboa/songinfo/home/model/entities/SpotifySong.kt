package ayds.lisboa.songinfo.home.model.entities

import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.view.LeapYearCheck

interface Song {
    val id: String
    val songName: String
    val artistName: String
    val albumName: String
    val releaseDate: String
    val spotifyUrl: String
    val imageUrl: String
    val releaseDatePrecision: String
    var isLocallyStored: Boolean
}

data class SpotifySong(
  override val id: String,
  override val songName: String,
  override val artistName: String,
  override val albumName: String,
  override val releaseDate: String,
  override val spotifyUrl: String,
  override val imageUrl: String,
  override val releaseDatePrecision: String,
  override var isLocallyStored: Boolean = false,

  private val dateFormat: DateFormat = DateFormatInjector.dateFormat
) : Song {

    val writeReleaseDatePrecision: String = dateFormat.writeReleaseDatePrecision(this)

}

object EmptySong : Song {
    override val id: String = ""
    override val songName: String = ""
    override val artistName: String = ""
    override val albumName: String = ""
    override val releaseDate: String = ""
    override val spotifyUrl: String = ""
    override val imageUrl: String = ""
    override val releaseDatePrecision: String = ""
    override var isLocallyStored: Boolean = false
}