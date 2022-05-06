package ayds.lisboa.songinfo.moredetails.controller

import ayds.lisboa.songinfo.moredetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.moredetails.view.OtherDetailsUiEvent
import ayds.lisboa.songinfo.moredetails.view.OtherDetailsView
import ayds.observer.Observer

interface OtherDetailsController {

    fun setOtherDetailsView(otherDetailsView: OtherDetailsView)

}

internal class OtherDetailsControllerImpl (
    private val otherDetailsModel: OtherDetailsModel
    ) : OtherDetailsController {

    private lateinit var otherDetailsView: OtherDetailsView

    override fun setOtherDetailsView(otherDetailsView: OtherDetailsView) {
        this.otherDetailsView = otherDetailsView
        otherDetailsView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherDetailsUiEvent> =
        Observer { value ->
            when (value) {
                OtherDetailsUiEvent.SearchBiography -> searchBiography()
               is OtherDetailsUiEvent.OpenBiographyArticleUrl -> openBiographyArticleUrl()
            }
        }

    private fun searchBiography(){
        Thread {
            otherDetailsModel.searchBiography(otherDetailsView.uiState.artistBiographyText)
        }.start()
    }

    private fun openBiographyArticleUrl() {
        otherDetailsView.openExternalLink(otherDetailsView.uiState.viewFullArticleUrl)
    }

}