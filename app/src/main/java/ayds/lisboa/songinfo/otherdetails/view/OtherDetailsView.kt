package ayds.lisboa.songinfo.otherdetails.view

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
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModelInjector
import ayds.lisboa.songinfo.otherdetails.model.entities.ArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyArtistBiography
import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography
import ayds.observer.Subject
import ayds.observer.Observable
import com.squareup.picasso.Picasso
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils

const val ARTIST_NAME_EXTRA ="artistName"
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

    private lateinit var biographyTextView: TextView
    private lateinit var viewFullArticleButton: Button
    private lateinit var imageView: ImageView

    override val uiEventObservable: Observable<OtherDetailsUiEvent> = onActionSubject
    override var uiState: OtherDetailsUiState = OtherDetailsUiState()

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initArtistName() //todo
        initProperties()
        initListeners()
        initObservers()

        notifySearchBiography()

        updateArtistUIImage()
      //  getArtistBiography() //TODO es necesario?

    }

    private fun initArtistName() {
       val artistName = getArtist()
        uiState = uiState.copy(artistName = artistName)

    }

    private fun initModule() {
        OtherDetailsViewInjector.init(this)
        otherDetailsModel = OtherDetailsModelInjector.getOtherDetailsModel()
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
            .subscribe { value -> this.updateArtistBiographyInfo(value) }
    }

    private fun updateArtistBiographyInfo(artistBiography: ArtistBiography) {
    //    getArtistBiography(artistBiography)
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

    private fun updateArtistBiographyUiState(artist : LastFMArtistBiography) {
        uiState = uiState.copy(
            artistName = artist.artist,
            viewFullArticleUrl = artist.url,
            artistBiographyText = biographyDescriptionHelper.getArtistBiographyText(artist),
            actionsEnabled = true
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            viewFullArticleUrl = "",
            artistBiographyText = biographyDescriptionHelper.getArtistBiographyText(),
            actionsEnabled = false
        )
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

    private fun getArtistBiography(artistBiography: ArtistBiography) {
        val artist = getArtist()
        getArtistBiographyForOtherWindow(artistBiography)
    }

    private fun getArtist(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA)?:"" //TODO como lo hacemos? :D
    }

        private fun getArtistBiographyForOtherWindow(artistName: ArtistBiography) {
            Thread {
                getArtistBiographyAndUpdateUI(artistName)
            }.start()
        }

        private fun getArtistBiographyAndUpdateUI(artistName: ArtistBiography) {
            updateArtistUIBiography(artistName.biography)
        }

    private fun updateArtistUIImage() {
        runOnUiThread {
            updateArtistImageURL()
        }
    }

    private fun updateArtistImageURL() {
        Picasso.get().load(IMAGE_URL_SERVICE).into(imageView)
    }

    private fun notifySearchBiography() {
        onActionSubject.notify(OtherDetailsUiEvent.SearchBiography)
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

    private fun setListenerUrlButton(urlBiography: String) { //todo
        viewFullArticleButton.setOnClickListener {
            navigateToUrl(urlBiography)
        }
    }

    private fun navigateToUrl(urlBiography: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlBiography)
        startActivity(intent)
    }

}