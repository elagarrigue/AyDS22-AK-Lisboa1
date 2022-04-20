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
import com.squareup.picasso.Picasso
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.gson.JsonElement
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder

interface OtherInfoWindow {
    fun onCreate(savedInstanceState: Bundle?)
}

const val ARTIST_NAME_EXTRA = "artistName"
private const val AUDIO_SCROBBLER_URL = "https://ws.audioscrobbler.com/2.0/"
private const val IMAGE_URL_LASTFM = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoWindowImpl : AppCompatActivity(), OtherInfoWindow {

    private lateinit var biographyTextView: TextView
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        open(intent.getStringExtra("artistName"))
    }

    private fun initProperties() {
        biographyTextView = findViewById(R.id.biographyTextView)
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            val finalText = createBiography(artistName)
            runOnUiThread {
                Picasso.get().load(IMAGE_URL_LASTFM).into(findViewById<View>(R.id.imageView) as ImageView)
                biographyTextView!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun createBiography(artistName: String?): String {
        var biographyText = DataBase.getInfo(dataBase, artistName)
        biographyText = if (biographyText != null) { // exists in db
            "[*]$biographyText"
        } else {
            getArtistBiographyFromLastFM(artistName)
        }
        return biographyText
    }

    private fun getLastFMAPI() = initRetrofit().create(LastFMAPI::class.java)

    private fun getArtistBiographyFromLastFM(artistName: String?): String {
        var biographyText: String = ""
        val callResponse: Response<String>
        try {
            callResponse = getLastFMAPI().getArtistInfo(artistName).execute()
            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].asJsonObject
            val bio = artist["bio"].asJsonObject
            val extract = bio["content"]
            val url = artist["url"]

            if (extract == null) {
                biographyText = "No Results"
            } else {
                biographyText = extract.asString.replace("\\n", "\n")
                biographyText = textToHtml(biographyText, artistName)

                // save to DB  <o/
                DataBase.saveArtist(dataBase, artistName, biographyText)
            }
            val urlString = jsonElementToString(url)

            findViewById<View>(R.id.openUrlButton).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlString)
                startActivity(intent)
            }
           return  biographyText
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
            return ""
        }
        //return biographyText
    }

    private fun initRetrofit() = Retrofit.Builder()
        .baseUrl(AUDIO_SCROBBLER_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase, "test", "sarasa")
        getArtistInfo(artist)
    }

    private fun jsonElementToString(jsonElement: JsonElement): String {
        return jsonElement.asString
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)" + term!!.toRegex(), "<b>" + term.uppercase() + "</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}