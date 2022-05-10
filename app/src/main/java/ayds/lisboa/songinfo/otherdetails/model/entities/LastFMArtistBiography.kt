package ayds.lisboa.songinfo.otherdetails.model.entities

interface ArtistBiography{
    val id : String
    val artist: String
    val biography: String
    val url: String
    var isLocallyStored: Boolean
}

data class LastFMArtistBiography(
    override val id: String,
    override val artist: String,
    override val biography: String,
    override val url: String,
    override var isLocallyStored: Boolean = false
): ArtistBiography {}

object EmptyArtistBiography : ArtistBiography {
    override val id: String = ""
    override val artist: String = ""
    override val biography: String = ""
    override val url: String = ""
    override var isLocallyStored: Boolean = false
}
