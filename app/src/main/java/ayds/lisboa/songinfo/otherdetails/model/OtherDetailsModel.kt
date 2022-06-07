package ayds.lisboa.songinfo.otherdetails.model

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherDetailsModel {
    val cardsObservable: Observable<List<Card>>

    fun searchCard(artistName: String)
}

internal class OtherDetailsModelImpl(private val repository: CardRepository) : OtherDetailsModel{

    override val cardsObservable = Subject<List<Card>>()

    override fun searchCard(artistName: String) {
        repository.getCardByArtist(artistName).let {
            cardsObservable.notify(it)
        }
    }
}