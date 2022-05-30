package ayds.newyork2.newyorkdata.external.nytimes

interface ExternalArtist {
    val artistName: String
    val artistInfo: String
    val artistUrl: String
    var isLocallyStored: Boolean
}

data class ExternalArtistInfo(
    override val artistName: String,
    override val artistInfo: String,
    override val artistUrl: String,
    override var isLocallyStored: Boolean = false
) : ExternalArtist

object ExternalEmptyArtist : ExternalArtist {
    override val artistName: String = ""
    override val artistInfo: String = ""
    override val artistUrl: String = ""
    override var isLocallyStored: Boolean = false
}