package ayds.lisboa.songinfo.otherdetails.view

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModel
import ayds.lisboa.songinfo.otherdetails.model.OtherDetailsModelInjector
import ayds.lisboa.songinfo.otherdetails.model.entities.*
import ayds.lisboa.songinfo.otherdetails.view.OtherDetailsUiState.Companion.IMAGE_URL_SERVICE
import ayds.observer.Subject
import ayds.observer.Observable
import com.squareup.picasso.Picasso
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils

const val ARTIST_NAME_EXTRA ="artistName"

interface OtherDetailsView {
    val uiEventObservable: Observable<OtherDetailsUiEvent>
    val uiState: OtherDetailsUiState

    fun onCreate(savedInstanceState: Bundle?)
    fun openExternalLink(url: String)
}

class OtherDetailsViewActivity : AppCompatActivity(), OtherDetailsView {

    private val onActionSubject = Subject<OtherDetailsUiEvent>()
    private lateinit var otherDetailsModel: OtherDetailsModel
    private val cardDescriptionHelper: CardDescriptionHelper = OtherDetailsViewInjector.biographyDescriptionHelper
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils
    private val convert : ConvertStringToHTML = OtherDetailsViewInjector.convertStringToHTML

    private lateinit var biographyTextView: TextView
    private lateinit var viewFullArticleButton: Button
    private lateinit var imageView: ImageView
    private lateinit var servicesSpinner: Spinner

    override val uiEventObservable: Observable<OtherDetailsUiEvent> = onActionSubject
    override var uiState: OtherDetailsUiState = OtherDetailsUiState()

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initArtistName()
        initProperties()
        initSpinner()
    }

    private fun initModule() {
        OtherDetailsViewInjector.init(this)
        otherDetailsModel = OtherDetailsModelInjector.getOtherDetailsModel()
    }

    private fun initArtistName() {
       val artistName = getArtist()
       uiState = uiState.copy(artistName = artistName)
    }

    private fun initProperties() {
        biographyTextView = findViewById(R.id.biographyTextView)
        viewFullArticleButton = findViewById<View>(R.id.viewFullArticleButton) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView
        servicesSpinner = findViewById(R.id.spServices)
    }

    private fun initSpinner(){
        val sourceToString = mutableMapOf<Source,String>()//TODO derivar la creacion del mapper a un metodo
        sourceToString[Source.LASTFM] = "LastFm"
        sourceToString[Source.WIKIPEDIA] = "Wikipedia"
        sourceToString[Source.NEW_YORK_TIMES] = "New York Times"

        val services = listOf(sourceToString[Source.LASTFM],sourceToString[Source.WIKIPEDIA],sourceToString[Source.NEW_YORK_TIMES])

        val spinnerAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,services)
        servicesSpinner.adapter = spinnerAdapter

        servicesSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, listPosition: Int, p3: Long) {
                /*
                listPosition=0=LastFM
                listPosition=1=Wikipedia
                listPosition=2=New York Times
                 */

                initListeners()
                initObservers()
                notifySearchBiography()
                updateArtistUIImage()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
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

    private fun updateArtistBiographyInfo(artistBiography: Card) {
        updateUiState(artistBiography)
        updateArtistBiographyDescription()
        updateArtistUIImage()
        updateViewFullArticleState()
    }

    private fun updateUiState(artistBiography: Card) {
        when (artistBiography) {
            is ServiceCard -> updateArtistBiographyUiState(artistBiography)
            is EmptyCard -> updateNoResultsUiState()
        }
    }

    private fun updateArtistBiographyUiState(artist : ServiceCard) {
        uiState = uiState.copy(
            artistName = artist.term,
            viewFullArticleUrl = artist.infoUrl,
            artistBiographyText = cardDescriptionHelper.getArtistBiographyText(artist),
            actionsEnabled = true
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            viewFullArticleUrl = "",
            artistBiographyText = cardDescriptionHelper.getArtistBiographyText(),
            actionsEnabled = false
        )
    }

    private fun updateArtistBiographyDescription(){
        runOnUiThread {
            updateBiographyTextView()
        }
    }

    private fun updateBiographyTextView(){
        val text = convert.convertTextToHtml(uiState.artistBiographyText, uiState.artistName)
        biographyTextView.text = setTextHTML(text)
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

    private fun notifySearchBiography() {
        onActionSubject.notify(OtherDetailsUiEvent.SearchBiography)
    }

    private fun updateArtistUIImage() {
        runOnUiThread {
            updateArtistImageURL()
        }
    }

    private fun updateArtistImageURL() {
        Picasso.get().load(IMAGE_URL_SERVICE).into(imageView)
    }

    private fun updateViewFullArticleState(){
        enableActions(uiState.actionsEnabled)
    }

    private fun enableActions(enable: Boolean) {
        runOnUiThread {
            viewFullArticleButton.isEnabled = enable
        }
    }

    private fun getArtist(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA)?:""
    }

}