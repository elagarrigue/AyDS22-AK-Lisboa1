package ayds.lisboa.songinfo.otherdetails.model.entities

interface Card {
    val description : String
    val infoUrl : String
    val source : String
    val sourceLogoUrl : String
}

data class ServiceCard (
    override val description : String,
    override val infoUrl : String,
    override val source: String,
    override val sourceLogoUrl: String,
): Card {}

object EmptyCard : Card {
    override val description : String = ""
    override val infoUrl: String = ""
    override val source: String = ""
    override val sourceLogoUrl: String = ""
}