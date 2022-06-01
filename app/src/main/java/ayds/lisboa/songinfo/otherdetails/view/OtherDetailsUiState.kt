package ayds.lisboa.songinfo.otherdetails.view

data class OtherDetailsUiState (
    val artistName: String = "",
    val viewFullArticleUrl: String = "",
    val artistBiographyText: String = "",
    val serviceSource: String = "",
    val actionsEnabled: Boolean = false
) {
    companion object {
        const val IMAGE_URL_SERVICE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    } //TODO creo que no deberia ir
}
