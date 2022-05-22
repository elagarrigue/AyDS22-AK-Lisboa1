package ayds.lisboa.songinfo.otherdetails.view

data class OtherDetailsUiState (
    val artistName: String = "",
    val viewFullArticleUrl: String = "",
    val artistBiographyText: String = "",
    val actionsEnabled: Boolean = false,
    val infoExternalServiceLabel: String = "LastFM", //todo label para indicar de donde viene el servicio
    // esta bien que sea una constante?

) {
    companion object {
        const val IMAGE_URL_SERVICE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}
