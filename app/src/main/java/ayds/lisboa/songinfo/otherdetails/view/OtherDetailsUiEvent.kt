package ayds.lisboa.songinfo.otherdetails.view

sealed class OtherDetailsUiEvent {
    object SearchBiography: OtherDetailsUiEvent() //TODO cual es la funcionalidad?
    object OpenBiographyArticleUrl : OtherDetailsUiEvent()
}