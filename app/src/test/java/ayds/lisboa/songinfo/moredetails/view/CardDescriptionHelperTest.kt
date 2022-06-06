package ayds.lisboa.songinfo.moredetails.view

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.view.CardDescriptionHelperImpl
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class CardDescriptionHelperTest {

    private val cardDescriptionHelper by lazy { CardDescriptionHelperImpl() }

    @Test
    fun `given a local artistBiography it should return the description`() {
        val artistBiography: Card = ServiceCard(
            "artist",
            "description",
            "infoUrl",
            "source",
            "sourceLogoUrl",
            true
        )

        val result = cardDescriptionHelper.getCardDescriptionText(artistBiography)

        val expected =
            "[*]\n" + "description"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local artistBiography it should return the description`() {
        val artistBiography: Card = ServiceCard(
            "artist",
            "description",
            "infoUrl",
            "source",
            "sourceLogoUrl",
            false
        )

        val result = cardDescriptionHelper.getCardDescriptionText(artistBiography)

        val expected =
            "\n" + "description"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non lastFM artistBiography it should return the artistBiography not found description`() {
        val artistBiography: Card = mockk()

        val result = cardDescriptionHelper.getCardDescriptionText(artistBiography)

        val expected = "No results"

        assertEquals(expected, result)
    }
}