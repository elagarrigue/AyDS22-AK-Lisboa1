package ayds.lisboa.songinfo.otherdetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa1.lastfm.LastFMService
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.CardLocalStorage

interface CardRepository{
    fun getArtistInfo(artistName : String) : Card
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val lastFMService: LastFMService //TODO esta bien lastFMService junto con Card?
) : CardRepository{

    override fun getArtistInfo(artistName: String): Card {
        var artistBiography = cardLocalStorage.getInfo(artistName)

        when {
            artistBiography != null -> markArtistBiographyAsLocal(artistBiography)
            else -> {
                try{
                    val serviceLastFMArtistBiography = lastFMService.getArtistBio(artistName)

                    serviceLastFMArtistBiography?.let{
                        artistBiography = ServiceCard(
                            it.artist,
                            it.biography,
                            it.url,
                            "", //TODO de donde se saca? //TODO seria un enumerado no?
                            "", //TODO idem anterior
                            it.isLocallyStored
                            )
                    }

                    artistBiography?.let{
                       cardLocalStorage.saveArtist(it)
                    }
                } catch(e : Exception){ //TODO eliminar el try catch en esta clase
                                            //en el peor caso retorna una lista vacia
                    artistBiography = null
                }
            }
        }
        return artistBiography ?: EmptyCard
    }

    private fun markArtistBiographyAsLocal(card: Card) {
        card.isLocallyStored = true
    }

}