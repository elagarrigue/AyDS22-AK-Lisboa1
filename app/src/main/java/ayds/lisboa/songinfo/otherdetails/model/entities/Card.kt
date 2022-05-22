package ayds.lisboa.songinfo.otherdetails.model.entities

interface CardExternalService { //todo mejor nombre interfaz
    val description : String
    val infoUrl : String
    val source : String
    val sourceLogoUrl : String
}

data class Card (
    override val description : String,
    override val infoUrl : String,
    override val source: String,
    override val sourceLogoUrl: String,
): CardExternalService {}

object EmptyCard : CardExternalService {
    override val description : String = ""
    override val infoUrl: String = ""
    override val source: String = ""
    override val sourceLogoUrl: String = ""
}