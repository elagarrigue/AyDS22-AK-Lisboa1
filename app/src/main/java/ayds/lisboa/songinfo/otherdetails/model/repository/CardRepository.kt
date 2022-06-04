package ayds.lisboa.songinfo.otherdetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.model.repository.external.Broker
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.CardLocalStorage

interface CardRepository{
    fun getArtistInfo(artistName : String) : List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository{

    override fun getArtistInfo(artistName: String): List<Card> {
        var listCards : List<Card> = cardLocalStorage.getInfo(artistName) //TODO ojo devuelve List<ServiceCard>

        when {
            listCards.isNotEmpty() -> markArtistBiographyAsLocal(listCards)
            else -> {
                    listCards = broker.getCards(artistName) //TODO reveer
                    if (listCards.isNotEmpty()){
                        saveCardsToArtist(listCards)
                    }
            }
        }
        return listCards
    }

    private fun markArtistBiographyAsLocal(listCards: List<Card>) {
        listCards.map { it.isLocallyStored = true}
    }

    private fun saveCardsToArtist(serviceCards: List<Card>){
        serviceCards.map { cardLocalStorage.saveArtist(it as ServiceCard)}
    }

}