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
import ayds.lisboa.songinfo.utils.UtilsInjector.navigationUtils //todo sospechoso
import ayds.observer.Subject
import ayds.observer.Observable
import com.squareup.picasso.Picasso

private const val IMAGE_URL_LASTFM = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface OtherDetailsView {
    val uiEventObservable: Observable<OtherDetailsUiEvent>
    val uiState: OtherDetailsUiState

    fun onCreate(savedInstanceState: Bundle?)
    fun openExternalLink(url: String)
}

class OtherDetailsViewActivity : AppCompatActivity(), OtherDetailsView {

    private val onActionSubject = Subject<OtherDetailsUiEvent>()
    private lateinit var otherDetailsModel: OtherDetailsModel

    private lateinit var biographyTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView

    override val uiEventObservable: Observable<OtherDetailsUiEvent> = onActionSubject
    override var uiState: OtherDetailsUiState = OtherDetailsUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        getArtistBiography()
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    private fun initProperties() {
        biographyTextView = findViewById(R.id.biographyTextView)
        openUrlButton = findViewById<View>(R.id.openUrlButton) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun getArtistBiography() {
        val artist = getArtist()
        getArtistBiographyForOtherWindow(artist)
    }

    private fun getArtist(): String? {
        return intent.getStringExtra(ARTIST_NAME_EXTRA)
    }

    private fun getArtistBiographyForOtherWindow(artistName: String?) {
        Thread {
            getArtistBiographyAndUpdateUI(artistName)
        }.start()
    }

    private fun getArtistBiographyAndUpdateUI(artistName: String?) {
        val artistBiography = getArtistInfo(artistName) //TODO
        updateArtistUIImage()
        updateArtistUIBiography(artistBiography)
    }

    private fun updateArtistUIImage() {
        runOnUiThread {
            updateArtistImageURL()
        }
    }

    private fun updateArtistImageURL() {
        Picasso.get().load(IMAGE_URL_LASTFM).into(imageView)
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
        openUrlButton.setOnClickListener {
            navigateToUrl(urlBiography)
        }
    }

    private fun navigateToUrl(urlBiography: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlBiography)
        startActivity(intent)
    }

    private fun convertBiographyTextToHtml(artistName: String?):String {
        return  textToHtml(replaceLineBreakToText(), artistName)
    }

    private fun replaceLineBreakToText(): String{
        return getBiographyExtract().replace("\\n", "\n") //esto va a venir del modelo o controlador
        //TODO
    }

    private fun textToHtml(text: String, term: String?): String {
        return  StringBuilder().apply {
            append("<html><div width=400>")
            append("<font face=\"arial\">")
            append(artistBiographyTextWithBold(text, term))
            append("</font></div></html>")
        }.toString()
    }

    private fun artistBiographyTextWithBold(text: String, term: String?): String {
        return text.apply {
            replace("'", " ")
            replace("\n", "<br>")
            replace("(?i)" + term!!.toRegex(), "<b>" + term.uppercase() + "</b>")
        }
    }







}