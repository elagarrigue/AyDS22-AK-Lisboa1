package ayds.lisboa.songinfo.moredetails.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.ARTIST_NAME_EXTRA
import ayds.lisboa.songinfo.moredetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.moredetails.model.OtherDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.moredetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography
import ayds.observer.Subject
import ayds.observer.Observable
import com.squareup.picasso.Picasso

import ayds.lisboa.songinfo.moredetails.model.repository.ArtistBiographyRepository
import ayds.lisboa.songinfo.moredetails.model.repository.ArtistBiographyRepositoryImpl
import ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm.*
import ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm.LastFMServiceImpl
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb.CursorToLastFMArtistBiographyMapper
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb.CursorToLastFMArtistBiographyMapperImpl
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils

private const val IMAGE_URL_SERVICE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface OtherDetailsView {
    val uiEventObservable: Observable<OtherDetailsUiEvent>
    val uiState: OtherDetailsUiState

    fun onCreate(savedInstanceState: Bundle?)
    fun openExternalLink(url: String)
}

class OtherDetailsViewActivity : AppCompatActivity(), OtherDetailsView {

    private val onActionSubject = Subject<OtherDetailsUiEvent>()
    private lateinit var otherDetailsModel: OtherDetailsModel
    private val biographyDescriptionHelper: BiographyDescriptionHelper = OtherDetailsViewInjector.biographyDescriptionHelper
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils


    private val lastFMToArtistBiographyResolver : LastFMToArtistBiographyResolver = JsonToArtistBiographyResolver()
    private val lastFMAPI : LastFMAPI = LastFMImpl().getLastFM()
    private val cursorToLastFMArtistBiographyMapper: CursorToLastFMArtistBiographyMapper = CursorToLastFMArtistBiographyMapperImpl()
    private val lastFMLocalStorage: LastFMLocalStorage = LastFMLocalStorageImpl(this, cursorToLastFMArtistBiographyMapper)
    private val lastFMService: LastFMService = LastFMServiceImpl(lastFMToArtistBiographyResolver,lastFMAPI)
    private val repository : ArtistBiographyRepository = ArtistBiographyRepositoryImpl(lastFMLocalStorage,lastFMService)


    private lateinit var biographyTextView: TextView
    private lateinit var viewFullArticleButton: Button
    private lateinit var imageView: ImageView

    override val uiEventObservable: Observable<OtherDetailsUiEvent> = onActionSubject
    override var uiState: OtherDetailsUiState = OtherDetailsUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initListeners()
        initObservers()
        updateArtistUIImage()
        getArtistBiography() //TODO es necesario?
    }

    private fun initModule() {
        OtherDetailsViewInjector.init(this)
        otherDetailsModel = OtherDetailsModelInjector.getOtherDetailsModel()
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    private fun initProperties() {
        biographyTextView = findViewById(R.id.biographyTextView)
        viewFullArticleButton = findViewById<View>(R.id.viewFullArticleButton) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun initListeners(){
        viewFullArticleButton.setOnClickListener { notifyOpenBiographyArticleUrl() }
    }

    private fun notifyOpenBiographyArticleUrl(){
        onActionSubject.notify(OtherDetailsUiEvent.OpenBiographyArticleUrl)
    }

    private fun initObservers() {
        otherDetailsModel.artistObservable
            .subscribe { value -> updateArtistBiographyInfo(value) }
    }

    private fun updateArtistBiographyInfo(artistBiography: ArtistBiography) {
        updateUiState(artistBiography)
        updateArtistBiographyDescription()
        updateArtistUIImage()//TODO Es necesario llamar al metodo otra vez? Ya se llama en el constructor
        updateViewFullArticleState()

    }

    private fun updateUiState(artistBiography: ArtistBiography) {
        when (artistBiography) {
            is LastFMArtistBiography -> updateArtistBiographyUiState(artistBiography)
            EmptyArtistBiography -> updateNoResultsUiState()
        }
    }

    private fun updateArtistBiographyDescription(){
        runOnUiThread {
            biographyTextView.text = uiState.artistBiographyText
        }
    }

    private fun updateViewFullArticleState(){
        enableActions(uiState.actionsEnabled)
    }

    private fun enableActions(enable: Boolean) {
        runOnUiThread {
            viewFullArticleButton.isEnabled = enable
        }
    }

    private fun getArtistBiography() {
        val artist = getArtist()
        getArtistBiographyForOtherWindow(artist)
    }

    private fun getArtist(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA)!! //TODO como lo hacemos? :D
    }

        private fun getArtistBiographyForOtherWindow(artistName: String) {
            Thread {
                getArtistBiographyAndUpdateUI(artistName)
            }.start()
        }

        private fun getArtistBiographyAndUpdateUI(artistName: String) {
            val artistBiography = repository.getArtistInfo(artistName) //TODO

            updateArtistUIBiography(artistBiography.biography)
        }

    private fun updateArtistUIImage() {
        runOnUiThread {
            updateArtistImageURL()
        }
    }

    private fun updateArtistImageURL() {
        Picasso.get().load(IMAGE_URL_SERVICE).into(imageView)
    }

    private fun updateArtistUIBiography(biographyText: String) {
        runOnUiThread {
            biographyTextView.text = setTextHTML(biographyText)
        }
    }

    private fun setTextHTML(html: String): Spanned {
        val result: Spanned =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        return result
    }

    private fun setListenerUrlButton(urlBiography: String) {
        viewFullArticleButton.setOnClickListener {
            navigateToUrl(urlBiography)
        }
    }

    private fun navigateToUrl(urlBiography: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlBiography)
        startActivity(intent)
    }

    private fun updateArtistBiographyUiState(artist : LastFMArtistBiography) {
        uiState = uiState.copy(
            viewFullArticleUrl = artist.url,
            artistBiographyText = biographyDescriptionHelper.getArtistBiographyText(artist),
            actionsEnabled = true
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            viewFullArticleUrl = "",
            artistBiographyText = biographyDescriptionHelper.getArtistBiographyText(),
            actionsEnabled = false
        )
    }
}