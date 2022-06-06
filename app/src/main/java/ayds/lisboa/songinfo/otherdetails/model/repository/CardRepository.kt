package ayds.lisboa.songinfo.otherdetails.model.repository

import android.util.Log //TODO BORRAR
import ayds.lisboa.songinfo.otherdetails.model.entities.Card
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
        for(card in serviceCards){
            if(card is ServiceCard){
                cardLocalStorage.saveArtist(card)
            }
            else{
            Log.e("msg","EmptyCard")
            //TODO no hace nada, borrar
            }
        }
        //serviceCards.map { cardLocalStorage.saveArtist(it as ServiceCard)}
    }

}