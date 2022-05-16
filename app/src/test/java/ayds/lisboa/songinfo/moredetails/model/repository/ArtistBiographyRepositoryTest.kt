package ayds.lisboa.songinfo.moredetails.model.repository

import ayds.lisboa.songinfo.otherdetails.model.repository.ArtistBiographyRepository
import ayds.lisboa.songinfo.otherdetails.model.repository.ArtistBiographyRepositoryImpl
import ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.otherdetails.model.repository.local.lastfm.LastFMLocalStorage
import io.mockk.mockk
import org.junit.Test

class ArtistBiographyRepositoryTest {

    private val lastFMLocalStorage: LastFMLocalStorage = mockk(relaxUnitFun = true)
    private val  lastFMService: LastFMService= mockk(relaxUnitFun = true)

    private val artistBiographyRepository: ArtistBiographyRepository by lazy {
        ArtistBiographyRepositoryImpl(lastFMLocalStorage, lastFMService)
    }

    @Test
}