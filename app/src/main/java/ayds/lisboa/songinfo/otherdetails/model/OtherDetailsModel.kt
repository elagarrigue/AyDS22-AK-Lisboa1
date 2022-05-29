package ayds.lisboa.songinfo.otherdetails.model

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherDetailsModel {
    val artistObservable: Observable<Card>

    fun searchBiography(artistName: String)
}

internal class OtherDetailsModelImpl(private val repository: CardRepository) : OtherDetailsModel{

    override val artistObservable = Subject<Card>()

    override fun searchBiography(artistName: String) {
        repository.getArtistInfo(artistName).let {
            artistObservable.notify(it)
        }
    }
}