package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.controller.HomeControllerInjector
import ayds.lisboa.songinfo.home.model.HomeModelInjector
import ayds.lisboa.songinfo.home.model.entities.DateFormat
import ayds.lisboa.songinfo.home.model.entities.DateFormatImpl

object HomeViewInjector {

    private val dateFormat: DateFormat = DateFormatImpl()
    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(dateFormat)

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}