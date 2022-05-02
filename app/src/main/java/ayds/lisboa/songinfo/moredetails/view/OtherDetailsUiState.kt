package ayds.lisboa.songinfo.moredetails.view

class OtherDetailsUiState (
    val ImageUrlLastFM: String = IMAGE_URL_LASTFM, //TODO nombre significativo?
    val viewFullArticleUrl: String = "",
    val actionsEnabled: Boolean = false, //TODO es siempre necesario?
) {

    companion object {
        const val IMAGE_URL_LASTFM = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"    }
}