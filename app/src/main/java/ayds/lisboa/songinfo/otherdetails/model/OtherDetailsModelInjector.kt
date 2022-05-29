package ayds.lisboa.songinfo.otherdetails.model

import android.content.Context
import ayds.lisboa.songinfo.otherdetails.model.repository.CardRepository
import ayds.lisboa.songinfo.otherdetails.model.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.CardLocalStorage
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.otherdetails.model.repository.local.service.sqldb.CardLocalStorageImpl
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsView
import ayds.lisboa1.lastfm.LastFMInjector
import ayds.lisboa1.lastfm.LastFMService

object OtherDetailsModelInjector {

    private lateinit var otherDetailsModel: OtherDetailsModel

    fun getOtherDetailsModel(): OtherDetailsModel = otherDetailsModel

    fun initOtherDetailsModel(otherDetailsView: OtherDetailsView) {
        val cardLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            otherDetailsView as Context, CursorToCardMapperImpl()
        )
        val lastFMService: LastFMService = LastFMInjector.lastFMService

        val repository: CardRepository =
            CardRepositoryImpl(cardLocalStorage, lastFMService)

        otherDetailsModel = OtherDetailsModelImpl(repository)
    }
}