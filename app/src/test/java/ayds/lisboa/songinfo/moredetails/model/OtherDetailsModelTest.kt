package ayds.lisboa.songinfo.moredetails.model

import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModelImpl
import ayds.lisboa.songinfo.otherdetails.model.repository.ArtistBiographyRepository
import io.mockk.mockk
import org.junit.Test

class OtherDetailsModelTest{

    private val repository : ArtistBiographyRepository = mockk()

    private val otherDetailsModel : OtherDetailsModel by lazy {
        OtherDetailsModelImpl(repository)
    }

    @Test
}