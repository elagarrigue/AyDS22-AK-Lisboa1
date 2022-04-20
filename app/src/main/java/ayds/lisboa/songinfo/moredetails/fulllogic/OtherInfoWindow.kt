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

class OtherInfoWindowImpl : AppCompatActivity(), OtherInfoWindow {

    private lateinit var biographyTextView: TextView
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        biographyTextView = findViewById(R.id.biographyTextView)
        open(intent.getStringExtra("artistName"))
    }

    private fun getArtistInfo(artistName: String?) {

        val lastFMAPI = initRetrofit().create(LastFMAPI::class.java)
        Thread {
            var biographyText = DataBase.getInfo(dataBase, artistName)
            if (biographyText != null) { // exists in db
                biographyText = "[*]$biographyText"
            } else {
                //definir funcion que recupere los datos

                // get from service
                val callResponse: Response<String>
                try {
                    callResponse = lastFMAPI.getArtistInfo(artistName).execute()
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
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            val imageUrl = //deberia ir arriba definida?
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            val finalText = biographyText
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
                biographyTextView!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun initRetrofit() = Retrofit.Builder()
        .baseUrl("https://ws.audioscrobbler.com/2.0/")
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