package ayds.lisboa.songinfo.otherdetails.view

import ayds.lisboa.songinfo.otherdetails.model.entities.*

private const val NO_RESULTS = "No results"
private const val LOCALLY_STORED = "[*]"
private const val NOT_LOCALLY_STORED = ""

interface CardDescriptionHelper {
    fun getArtistBiographyText(card: Card = EmptyCard) : String
}

internal class CardDescriptionHelperImpl() : CardDescriptionHelper{

    override fun getArtistBiographyText(card: Card): String {
        return when (card) {
            is ServiceCard ->
                "${
                    if (card.isLocallyStored)
                        LOCALLY_STORED
                    else
                        NOT_LOCALLY_STORED
                }\n" +
                        if(card.description.isEmpty()){
                            NO_RESULTS
                        }else{
                            "${card.description}" //TODO se puede mejorar la expresion ?
                        }
            else -> NO_RESULTS
        }
    }
}