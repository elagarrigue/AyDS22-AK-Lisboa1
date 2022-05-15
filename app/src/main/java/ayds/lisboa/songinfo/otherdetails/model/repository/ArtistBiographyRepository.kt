package ayds.lisboa.songinfo.otherdetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.otherdetails.model.repository.local.lastfm.LastFMLocalStorage

interface ArtistBiographyRepository{
    fun getArtistInfo(artistName : String) : ArtistBiography
}

internal class ArtistBiographyRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : ArtistBiographyRepository{

    override fun getArtistInfo(artistName: String): ArtistBiography {
        var artistBiography = lastFMLocalStorage.getInfo(artistName)

        when {
            artistBiography != null -> markArtistBiographyAsLocal(artistBiography)
            else -> {
                try{
                    artistBiography = lastFMService.getArtistBio(artistName)

                    artistBiography?.let{
                       lastFMLocalStorage.saveArtist(it)
                    }
                } catch(e : Exception){
                    artistBiography = null
                }
            }
        }
        return artistBiography ?: EmptyArtistBiography
    }

    private fun markArtistBiographyAsLocal(artistBiography: ArtistBiography) {
        artistBiography.isLocallyStored = true
    }

}