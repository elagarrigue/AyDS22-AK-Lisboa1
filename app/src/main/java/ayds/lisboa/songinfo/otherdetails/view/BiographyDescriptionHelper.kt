package ayds.lisboa.songinfo.otherdetails.view

import ayds.lisboa.songinfo.otherdetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography

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