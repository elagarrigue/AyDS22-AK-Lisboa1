package ayds.lisboa.songinfo.moredetails.view

import ayds.lisboa.songinfo.moredetails.controller.OtherDetailsControllerInjector
import ayds.lisboa.songinfo.moredetails.model.OtherDetailsModelInjector

object OtherDetailsViewInjector {

    val biographyDescriptionHelper: BiographyDescriptionHelper = BiographyDescriptionHelperImpl()

    fun init(otherDetailsView: OtherDetailsView) {
        OtherDetailsModelInjector.initOtherDetailsModel(otherDetailsView)
        OtherDetailsControllerInjector.onViewStarted(otherDetailsView)
    }
}