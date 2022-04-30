package ayds.lisboa.songinfo.moredetails.model.entities

interface ArtistBiography{
    val artist: String
    val biography: String
    val extract: String
    val url: String
}

data class LastFMArtistBiography(
    override val artist: String,
    override val biography: String,
    override val extract: String,
    override val url: String
): ArtistBiography {}

object EmptyArtistBiography : ArtistBiography {
    override val artist: String = ""
    override val biography: String = ""
    override val extract: String = ""
    override val url: String = ""
}
