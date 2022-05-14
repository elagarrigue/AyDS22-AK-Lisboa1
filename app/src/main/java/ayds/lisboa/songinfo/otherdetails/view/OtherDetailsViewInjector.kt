package ayds.lisboa.songinfo.otherdetails.view

import ayds.lisboa.songinfo.otherdetails.controller.OtherDetailsControllerInjector
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModelInjector

object OtherDetailsViewInjector {

    val biographyDescriptionHelper: BiographyDescriptionHelper = BiographyDescriptionHelperImpl()

    val convertStringToHTML: ConvertStringToHTML = ConvertStringToHTMLImpl()

    fun init(otherDetailsView: OtherDetailsView) {
        OtherDetailsModelInjector.initOtherDetailsModel(otherDetailsView)
        OtherDetailsControllerInjector.onViewStarted(otherDetailsView)
    }
}