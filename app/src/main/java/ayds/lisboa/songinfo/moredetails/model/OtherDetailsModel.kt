package ayds.lisboa.songinfo.moredetails.model

import ayds.lisboa.songinfo.moredetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.moredetails.model.repository.ArtistBiographyRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherDetailsModel {

    val artistObservable: Observable<ArtistBiography>

    fun searchBiography(artistName: String)

}

internal class OtherDetailsModelImpl(private val repository: ArtistBiographyRepository) : OtherDetailsModel{

    override val artistObservable = Subject<ArtistBiography>()

    override fun searchBiography(artistName: String) {
        repository.getArtistInfo(artistName).let {
            artistObservable.notify(it)
        }
    }
}