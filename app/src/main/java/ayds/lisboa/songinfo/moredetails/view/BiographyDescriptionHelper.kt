package ayds.lisboa.songinfo.moredetails.view

import ayds.lisboa.songinfo.moredetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.moredetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography

interface BiographyDescriptionHelper {
    fun getArtistBiographyText(artistBiography: ArtistBiography = EmptyArtistBiography) : String
}
internal class BiographyDescriptionHelperImpl() : BiographyDescriptionHelper{
    override fun getArtistBiographyText(artistBiography: ArtistBiography): String {
        return when (artistBiography) {
            is LastFMArtistBiography ->
                "${
                    if (artistBiography.isLocallyStored)
                        "[*]"
                    else
                        ""
                }\n" +
                "${artistBiography.biography}"
            else -> "No results"
        }
    }
}