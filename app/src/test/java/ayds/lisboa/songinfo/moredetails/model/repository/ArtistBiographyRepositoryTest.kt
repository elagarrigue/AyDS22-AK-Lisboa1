package ayds.lisboa.songinfo.moredetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.repository.ArtistBiographyRepository
import ayds.lisboa.songinfo.otherdetails.model.repository.ArtistBiographyRepositoryImpl
import ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.otherdetails.model.repository.local.lastfm.LastFMLocalStorage
import io.mockk.every
import org.junit.Assert.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ArtistBiographyRepositoryTest {

    private val lastFMLocalStorage: LastFMLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService: LastFMService= mockk(relaxUnitFun = true)

    private val artistBiographyRepository: ArtistBiographyRepository by lazy {
        ArtistBiographyRepositoryImpl(lastFMLocalStorage, lastFMService)
    }

    @Test
    fun `given existing artistBiography by name should return artistBiography and mark it as local`() {
        val artistBiography = LastFMArtistBiography("artist", "biography", "url", false)
        every { lastFMLocalStorage.getInfo("artist") } returns artistBiography

        val result = artistBiographyRepository.getArtistInfo("artist")

        assertEquals(artistBiography, result)
        assertTrue(artistBiography.isLocallyStored)
    }

    @Test
    fun `given non existing artistBiography by name should get the artistBiography and store it`() {
        val artistBiography = LastFMArtistBiography("artist", "biography", "url", false)
        every { lastFMLocalStorage.getInfo("artist") } returns null
        every { lastFMService.getArtistBio("artist") } returns artistBiography

        val result = artistBiographyRepository.getArtistInfo("artist")

        assertEquals(artistBiography, result)
        assertFalse(artistBiography.isLocallyStored)
        verify { lastFMLocalStorage.saveArtist(artistBiography) }
    }

    @Test
    fun `given non existing artistBiography by term should return empty artistBiography`() {
        every { lastFMLocalStorage.getInfo("artist") } returns null
        every { lastFMService.getArtistBio("artist") } returns null

        val result = artistBiographyRepository.getArtistInfo("artist")

        assertEquals(EmptyArtistBiography, result)
    }

    @Test
    fun `given service exception should return empty artistBiography`() {
        every { lastFMLocalStorage.getInfo("artist") } returns null
        every { lastFMService.getArtistBio("artist") } throws mockk<Exception>()

        val result = artistBiographyRepository.getArtistInfo("artist")

        assertEquals(EmptyArtistBiography, result)
    }
}