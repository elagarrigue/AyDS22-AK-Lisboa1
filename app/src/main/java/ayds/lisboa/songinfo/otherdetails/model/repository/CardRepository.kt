package ayds.lisboa.songinfo.otherdetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.model.repository.external.Broker
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.CardLocalStorage

interface CardRepository{
    fun getArtistInfo(artistName : String) : Card
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository{

    override fun getArtistInfo(artistName: String): Card {
        var card = cardLocalStorage.getInfo(artistName)

        when {
            card != null -> markArtistBiographyAsLocal(card)
            else -> {
                    val serviceCards = broker.getCards(artistName)
                    serviceCards?.let{
                    }

                    card?.let{
                       cardLocalStorage.saveArtist(it)
                    }
            }
        }
        return card ?: EmptyCard
    }

    private fun markArtistBiographyAsLocal(card: Card) {
        card.isLocallyStored = true
    }

}