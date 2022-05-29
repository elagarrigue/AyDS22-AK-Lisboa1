package ayds.lisboa.songinfo.otherdetails.model.entities

interface Card {
    val artist: String //TODO esta bien que tenga artist Card?
    val description : String
    val infoUrl : String
    val source : String
    val sourceLogoUrl : String
    var isLocallyStored: Boolean //TODO esta bien que tenga esto?
}

data class ServiceCard (
    override val artist : String,
    override val description : String,
    override val infoUrl : String,
    override val source: String,
    override val sourceLogoUrl: String,
    override var isLocallyStored: Boolean = false
): Card {}

object EmptyCard : Card {
    override val artist : String = ""
    override val description : String = ""
    override val infoUrl: String = ""
    override val source: String = ""
    override val sourceLogoUrl: String = ""
    override var isLocallyStored: Boolean = false
}