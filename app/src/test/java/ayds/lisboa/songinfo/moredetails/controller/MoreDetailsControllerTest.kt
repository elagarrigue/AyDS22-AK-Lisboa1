package ayds.lisboa.songinfo.moredetails.controller

import ayds.lisboa.songinfo.otherdetails.controller.OtherDetailsControllerImpl
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsUiEvent
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsView
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
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
    fun ' ' () {

    }
}