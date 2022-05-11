package ayds.lisboa.songinfo.otherdetails.view

import ayds.lisboa.songinfo.otherdetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography

private const val NO_RESULTS = "No results"
private const val LOCALLY_STORED = "[*]"
private const val NOT_LOCALLY_STORED = ""

interface BiographyDescriptionHelper {
    fun getArtistBiographyText(artistBiography: ArtistBiography = EmptyArtistBiography) : String
}

internal class BiographyDescriptionHelperImpl() : BiographyDescriptionHelper{

    override fun getArtistBiographyText(artistBiography: ArtistBiography): String {
        return when (artistBiography) {
            is LastFMArtistBiography ->
                "${
                    if (artistBiography.isLocallyStored)
                        LOCALLY_STORED
                    else
                        NOT_LOCALLY_STORED
                }\n" +
                "${artistBiography.biography}"
            else -> NO_RESULTS
        }
    }
}