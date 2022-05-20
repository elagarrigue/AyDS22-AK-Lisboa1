package ayds.lisboa.songinfo.moredetails.view

import ayds.lisboa.songinfo.otherdetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography
import ayds.lisboa.songinfo.otherdetails.view.BiographyDescriptionHelperImpl
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BiographyDescriptionHelperTest {

    private val biographyDescriptionHelper by lazy { BiographyDescriptionHelperImpl() }

    @Test
    fun `given a local artistBiography it should return the description`() {
        val artistBiography: ArtistBiography = LastFMArtistBiography(
            "artist",
            "biography",
            "url",
            true
        )

        val result = biographyDescriptionHelper.getArtistBiographyText(artistBiography)

        val expected =
            "[*]\n" + "biography"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local artistBiography it should return the description`() {
        val artistBiography: ArtistBiography = LastFMArtistBiography(
            "artist",
            "biography",
            "url",
            false
        )

        val result = biographyDescriptionHelper.getArtistBiographyText(artistBiography)

        val expected =
            "\n" + "biography"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non lastFM artistBiography it should return the artistBiography not found description`() {
        val artistBiography: ArtistBiography = mockk()

        val result = biographyDescriptionHelper.getArtistBiographyText(artistBiography)

        val expected = "No results"

        assertEquals(expected, result)
    }
}