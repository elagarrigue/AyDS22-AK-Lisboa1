package ayds.lisboa.songinfo.moredetails.view

sealed class OtherDetailsUiEvent {
    object SearchBiography: OtherDetailsUiEvent() //TODO cual es la funcionalidad?
    object OpenBiographyArticleUrl : OtherDetailsUiEvent()
}