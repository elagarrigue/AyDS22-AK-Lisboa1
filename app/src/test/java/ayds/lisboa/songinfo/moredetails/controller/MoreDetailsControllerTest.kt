package ayds.lisboa.songinfo.moredetails.controller

import ayds.lisboa.songinfo.otherdetails.controller.OtherDetailsControllerImpl
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsUiEvent
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsUiState
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsView
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsControllerTest {

    private val otherDetailsModel: OtherDetailsModel = mockk(relaxUnitFun = true)

    private val onActionSubject = Subject<OtherDetailsUiEvent>()
    private val otherDetailsView: OtherDetailsView = mockk(relaxUnitFun = true) {
        every { uiEventObservable } returns onActionSubject
    }

    private val otherDetailsController by lazy {
        OtherDetailsControllerImpl(otherDetailsModel)
    }

    @Before
    fun setup() {
        otherDetailsController.setOtherDetailsView(otherDetailsView)
    }

    @Test
    fun `on search event should search biography`() {
        every { otherDetailsView.uiState } returns OtherDetailsUiState(artistName = "biography")

        onActionSubject.notify(OtherDetailsUiEvent.SearchBiography)

        verify { otherDetailsModel.searchBiography("biography") }
    }

    @Test
    fun `on open full article url event should open external link`  () {
        every { otherDetailsView.uiState } returns OtherDetailsUiState(viewFullArticleUrl = "url")

        onActionSubject.notify(OtherDetailsUiEvent.OpenBiographyArticleUrl)

        verify { otherDetailsView.openExternalLink("url") }
    }
}