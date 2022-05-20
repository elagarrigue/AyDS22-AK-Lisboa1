package ayds.lisboa.songinfo.moredetails.model

import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModelImpl
import ayds.lisboa.songinfo.otherdetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.repository.ArtistBiographyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherDetailsModelTest{

    private val repository : ArtistBiographyRepository = mockk()

    private val otherDetailsModel : OtherDetailsModel by lazy {
        OtherDetailsModelImpl(repository)
    }

    @Test
    fun `on search biography it should notify the result`() {
        val artistBiography: ArtistBiography = mockk()
        every { repository.getArtistInfo("artist") } returns artistBiography
        val artistBiographyTester: (ArtistBiography) -> Unit = mockk(relaxed = true)
        otherDetailsModel.artistObservable.subscribe {
            artistBiographyTester(it)
        }

        otherDetailsModel.searchBiography("artist")

        verify { artistBiographyTester (artistBiography) }
    }
}