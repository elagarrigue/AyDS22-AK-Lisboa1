package ayds.lisboa.songinfo.otherdetails.model.repository.external

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.model.entities.Source
import ayds.winchester2.wikipedia.ExternalRepository

internal class ProxyWikipedia (
    private val wikipediaService : ExternalRepository
    ) : ProxyCard {

        override fun getCard(artist: String) : Card {
            var cardLastFM : ServiceCard? = null
            try {
                val dataCardLastFM = wikipediaService.getArtistDescription(artist)
                if (dataCardLastFM != null) {
                    cardLastFM = ServiceCard(
                        "", //TODO Wikipedia no retorna artista?
                        dataCardLastFM.description,
                        dataCardLastFM.source,
                        Source.WIKIPEDIA,
                        dataCardLastFM.sourceLogo
                    )
                }
            } catch (e: Exception) {
                cardLastFM = null
            }
            return cardLastFM ?: EmptyCard
        }
}