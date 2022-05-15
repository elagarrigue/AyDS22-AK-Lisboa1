package ayds.lisboa.songinfo.otherdetails.model.entities

interface ArtistBiography{
    val artist: String
    val biography: String
    val url: String
    var isLocallyStored: Boolean
}

data class LastFMArtistBiography(
    override val artist: String,
    override val biography: String,
    override val url: String,
    override var isLocallyStored: Boolean = false
): ArtistBiography {}

object EmptyArtistBiography : ArtistBiography {
    override val artist: String = ""
    override val biography: String = ""
    override val url: String = ""
    override var isLocallyStored: Boolean = false
}
