package ayds.lisboa.songinfo.moredetails.controller

import ayds.lisboa.songinfo.moredetails.model.OtherDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.view.OtherDetailsView

object OtherDetailsControllerInjector {
    fun onViewStarted(otherDetailsView: OtherDetailsView) {
        OtherDetailsControllerImpl(OtherDetailsModelInjector.getOtherDetailsModel()).apply {
            setOtherDetailsView(otherDetailsView)
        }
    }
}