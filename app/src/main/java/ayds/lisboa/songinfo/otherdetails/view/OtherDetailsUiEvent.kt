package ayds.lisboa.songinfo.otherdetails.view

sealed class OtherDetailsUiEvent {
    object SearchBiography: OtherDetailsUiEvent()
    object OpenBiographyArticleUrl : OtherDetailsUiEvent()
}