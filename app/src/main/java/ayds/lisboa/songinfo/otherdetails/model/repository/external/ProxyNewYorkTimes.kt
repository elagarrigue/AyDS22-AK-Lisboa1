package ayds.lisboa.songinfo.otherdetails.model.repository.external

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.model.entities.Source
import ayds.newyork2.newyorkdata.external.nytimes.NYTimesService

internal class ProxyNewYorkTimes (
    private val newYorkTimesService : NYTimesService
) : ProxyCard{

    override fun getCard(artist: String) : Card {
        var cardNewYork : ServiceCard? = null
        try {
            val dataCardNewYork = newYorkTimesService.getArtist(artist)
            if (dataCardNewYork != null) {
                cardNewYork = ServiceCard(
                    dataCardNewYork.artistName,
                    dataCardNewYork.artistInfo,
                    dataCardNewYork.artistUrl,
                    Source.NEW_YORK_TIMES,
                    "" //TODO deberiamos poder obtener el logoURl
                )
            }
        } catch (e: Exception) {
            cardNewYork = null
        }
        return cardNewYork ?: EmptyCard
    }
}