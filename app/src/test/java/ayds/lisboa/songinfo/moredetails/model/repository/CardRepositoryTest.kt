package ayds.lisboa.songinfo.moredetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.model.repository.CardRepository
import ayds.lisboa.songinfo.otherdetails.model.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.CardLocalStorage
import io.mockk.MockKStubScope
import io.mockk.every
import org.junit.Assert.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class CardRepositoryTest {

    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService: ayds.lisboa1.lastfm.LastFMService = mockk(relaxUnitFun = true)

    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(cardLocalStorage, lastFMService)
    }

    @Test
    fun `given existing artistBiography by name should return artistBiography and mark it as local`() {
        val artistBiography = ServiceCard("artist", "description", "infoUrl","source", "sourceLogoUrl", false)
        every { cardLocalStorage.getInfo("artist") } returns artistBiography

        val result = cardRepository.getArtistInfo("artist")

        assertEquals(artistBiography, result)
        assertTrue(artistBiography.isLocallyStored)
    }

    @Test
    fun `given non existing artistBiography by name should get the artistBiography and store it`() {
        val artistBiography = ServiceCard("artist", "description", "infoUrl","source", "sourceLogoUrl", false)
        every { cardLocalStorage.getInfo("artist") } returns null
        every { lastFMService.getArtistBio("artist") } returns artistBiography

        val result = cardRepository.getArtistInfo("artist")

        assertEquals(artistBiography, result)
        assertFalse(artistBiography.isLocallyStored)
        var serviceCard = ServiceCard(
            artistBiography.artist,
            artistBiography.description,
            artistBiography.infoUrl,
            "", //TODO de donde viene?
            "",
            artistBiography.isLocallyStored
        ) //TODO esto esta bien?

        verify { cardLocalStorage.saveArtist(serviceCard) } //TODO ESTO esta bien?
    }

    @Test
    fun `given non existing artistBiography by term should return empty artistBiography`() {
        every { cardLocalStorage.getInfo("artist") } returns null
        every { lastFMService.getArtistBio("artist") } returns null

        val result = cardRepository.getArtistInfo("artist")

        assertEquals(EmptyCard, result)
    }

    @Test
    fun `given service exception should return empty artistBiography`() {
        every { cardLocalStorage.getInfo("artist") } returns null
        every { lastFMService.getArtistBio("artist") } throws mockk<Exception>()

        val result = cardRepository.getArtistInfo("artist")

        assertEquals(EmptyCard, result)
    }
}

private infix fun <T, B> MockKStubScope<T, B>.returns(artistBiography: ServiceCard) {

}
