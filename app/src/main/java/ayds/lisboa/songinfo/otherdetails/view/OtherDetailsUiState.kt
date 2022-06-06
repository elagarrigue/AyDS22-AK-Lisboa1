package ayds.lisboa.songinfo.otherdetails.view

import ayds.lisboa.songinfo.otherdetails.model.entities.Source

data class OtherDetailsUiState (
    val artistName: String = "",
    val selectedCardPosition: Int = 0, //Todo revisar
    var listCards: List<CardUi> = listOf(),
    val actionsEnabled: Boolean = false
) {
}

data class CardUi(
    val urlLogo: String = "",
    val description: String = "",
    val urlSource: String = "",
    val source: Source = Source.EMPTY
)
{
}