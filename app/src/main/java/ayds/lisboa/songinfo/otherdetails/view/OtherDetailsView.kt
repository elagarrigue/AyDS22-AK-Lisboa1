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
        initListeners()
        initObservers()
        notifySearchBiography()
        updateSpinner()
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

    private fun updateSpinner(){
        val services = listOf<String>()
        for(card in uiState.listCards)
            services.plus(card.source)

        val spinnerAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,services)
        servicesSpinner.adapter = spinnerAdapter

        servicesSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, listPosition: Int, p3: Long) {
                /*
                listPosition=0=LastFM
                listPosition=1=New York Times
                listPosition=2=Wikipedia
                 */
                if(uiState.listCards.isNotEmpty())
                    updateArtistBiographyInfoAux()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //TODO
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

    private fun updateArtistBiographyInfo(artistBiography: List<Card>) {
        if(artistBiography.isNotEmpty()) {
            updateSourceList(artistBiography)
        }else
            updateNoResultsUiState()

    }

    private fun updateSourceList(listCard: List<Card>){
        for (card in listCard){
            if(card is ServiceCard)
                uiState.listCards.plus(cardToUiCard(card))
        }
    }

    private fun cardToUiCard(card: Card): CardUi{
        return CardUi(
            card.sourceLogoUrl,
            card.description,
            card.infoUrl
        )
    }

    private fun updateArtistBiographyInfoAux(){
        updateArtistBiographyUiState()
        updateArtistBiographyDescription()
        updateArtistUIImage(uiState.listCards[servicesSpinner.selectedItemPosition])
        updateViewFullArticleState()
    }

    private fun updateArtistBiographyUiState() {
        uiState = uiState.copy(
            artistName = uiState.artistName,
            selectedCardPosition = servicesSpinner.selectedItemPosition,
            actionsEnabled = true
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            selectedCardPosition = 0, //TODO checkear posicion vacia
            actionsEnabled = false
        )
    }

    private fun updateArtistBiographyDescription(){
        runOnUiThread {
            updateBiographyTextView()
        }
    }

    private fun updateBiographyTextView(){
        val text = convert.convertTextToHtml(uiState.listCards[servicesSpinner.selectedItemPosition].description, uiState.artistName)
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

    private fun updateArtistUIImage(artistBiography: CardUi) {
        runOnUiThread {
            updateArtistImageURL(artistBiography)
        }
    }

    private fun updateArtistImageURL(artistBiography: CardUi) {
        Picasso.get().load(artistBiography.urlLogo).into(imageView)
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