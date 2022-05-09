package ayds.lisboa.songinfo.moredetails.model

import android.content.Context
import ayds.lisboa.songinfo.moredetails.model.repository.ArtistBiographyRepository
import ayds.lisboa.songinfo.moredetails.model.repository.ArtistBiographyRepositoryImpl
import ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm.LastFMInjector
import ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb.CursorToLastFMArtistBiographyMapperImpl
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.view.OtherDetailsView

object OtherDetailsModelInjector {

    private lateinit var otherDetailsModel: OtherDetailsModel

    fun getOtherDetailsModel(): OtherDetailsModel = otherDetailsModel

    fun initOtherDetailsModel(otherDetailsView: OtherDetailsView) {
        val lastFMLocalStorage: LastFMLocalStorage = LastFMLocalStorageImpl(
            otherDetailsView as Context, CursorToLastFMArtistBiographyMapperImpl()
        )
        val lastFMService: LastFMService = LastFMInjector.lastFMService

        val repository: ArtistBiographyRepository =
            ArtistBiographyRepositoryImpl(lastFMLocalStorage, lastFMService)

        otherDetailsModel = OtherDetailsModelImpl(repository)
    }
}