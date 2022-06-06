package ayds.lisboa.songinfo.otherdetails.view

data class OtherDetailsUiState (
    val artistName: String = "",
    var listCards: List<CardUi> = listOf(),
    val actionsEnabled: Boolean = false
) {
}

data class CardUi(
    val urlLogo: String = "",
    val description: String = "",
    val urlSource: String = "",
)
{
}