package ayds.lisboa.songinfo.moredetails.view

data class OtherDetailsUiState (
    val viewFullArticleUrl: String = "",
    val artistBiographyText: String = "",
    val actionsEnabled: Boolean = false,
) {

    companion object {
        const val IMAGE_URL_SERVICE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}
