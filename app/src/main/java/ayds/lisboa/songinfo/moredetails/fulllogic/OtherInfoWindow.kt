package ayds.lisboa.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import ayds.lisboa.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import android.content.Intent
import android.net.Uri
import android.os.Build.*
import com.squareup.picasso.Picasso
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.ImageView
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import android.widget.Button

const val ARTIST_NAME_EXTRA = "artistName"
private const val LASTFM_API = "https://ws.audioscrobbler.com/2.0/"
private const val IMAGE_URL_LASTFM = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val ARTIST = "artist"
private const val ARTIST_BIOGRAPHY = "bio"
private const val ARTIST_BIOGRAPHY_EXTRACT = "content"
private const val ARTIST_BIOGRAPHY_URL = "url"

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var biographyTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var imageView: ImageView
    private lateinit var dataBase: DataBase
    private lateinit var biographyJsonObject: JsonObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        open(intent.getStringExtra("artistName"))
    }

    private fun initProperties() {
        biographyTextView = findViewById(R.id.biographyTextView)
        openUrlButton = findViewById<View>(R.id.openUrlButton) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            val artistBiography = createBiography(artistName)
            updateArtistImage()
            updateArtistBiography(artistBiography)
        }.start()
    }

    private fun updateArtistImage() {
        runOnUiThread {
            updateArtistImageURL()
        }
    }

    private fun updateArtistImageURL() {
        Picasso.get().load(IMAGE_URL_LASTFM).into(imageView)
    }

    private fun updateArtistBiography(biographyText: String) {
        runOnUiThread {
            biographyTextView.text = setTextHTML(biographyText)
        }
    }

    private fun setTextHTML(html: String): Spanned {
        val result: Spanned =
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        return result
    }

    private fun createBiography(artistName: String?): String {
        val biographyText = dataBase.getInfo(artistName)
        return if (biographyText != null) "[*]$biographyText" else getArtistBiographyFromLastFM(artistName)
    }

    private fun getLastFMAPI() = initRetrofit().create(LastFMAPI::class.java)

    private fun getArtistBiographyFromLastFM(artistName: String?): String {
        getArtistJSon(artistName)
        setListenerUrlButton(getBiographyUrl())
        return getArtistBiographyText(artistName)
    }

    private fun getArtistBiographyText(artistName: String?): String {
        val biographyText: String = if (getBiographyExtract().isEmpty()) {
            "No Results"
        } else {
            updateArtistBiographyText(artistName)
        }
        return biographyText
    }

    private fun updateArtistBiographyText(artistName: String?): String {
        var biographyText: String?
        biographyText = getBiographyExtract().replace("\\n", "\n")
        biographyText = textToHtml(biographyText, artistName)
        dataBase.saveArtist(artistName, biographyText)
        return biographyText
    }

    private fun getArtistJSon(artistName: String?) {
        val callResponse: Response<String>
        try {
            callResponse = getLastFMAPI().getArtistInfo(artistName).execute()
            val gson = Gson()
            biographyJsonObject = gson.fromJson(callResponse.body(), JsonObject::class.java)
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
    }

    private fun getBiographyExtract(): String {
        val artist = biographyJsonObject[ARTIST].asJsonObject
        val bio = artist[ARTIST_BIOGRAPHY].asJsonObject
        return bio[ARTIST_BIOGRAPHY_EXTRACT].asString
    }

    private fun getBiographyUrl(): String {
        val artist = biographyJsonObject[ARTIST].asJsonObject
        return artist[ARTIST_BIOGRAPHY_URL].asString
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

    private fun initRetrofit() = Retrofit.Builder()
        .baseUrl(LASTFM_API)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        dataBase.saveArtist("test", "sarasa")
        getArtistInfo(artist)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        builder.append(artistBiographyTextWithBold(text, term))
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun artistBiographyTextWithBold(text: String, term: String?): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + term!!.toRegex(), "<b>" + term.uppercase() + "</b>")
    }
}