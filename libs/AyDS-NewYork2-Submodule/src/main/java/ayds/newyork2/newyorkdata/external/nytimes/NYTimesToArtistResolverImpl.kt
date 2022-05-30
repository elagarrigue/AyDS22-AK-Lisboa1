package ayds.newyork2.newyorkdata.external.nytimes

import com.google.gson.Gson
import com.google.gson.JsonObject

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT  = "abstract"
private const val WEB_URL = "web_url"


class NYTimesToArtistResolverImpl : NYTimesToArtistResolver {

    override fun getArtistFromExternalData(serviceData: String?, artistName: String) : ExternalArtistInfo? {
        return try {
            serviceData?.getFirstItem()?.let { item ->
                ExternalArtistInfo(
                    artistName,
                    item.getInfo(),
                    item.getUrl()
                )
            }
        } catch(e : Exception){
            null
        }
    }

    private fun JsonObject.getInfo(): String {
        return this[ABSTRACT].asString
    }

    private fun JsonObject.getUrl(): String {
        return this[WEB_URL].asString
    }

    private fun String?.getFirstItem() : JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        val resp = jsonObject[RESPONSE].asJsonObject
        val articles = resp[DOCS].asJsonArray
        return articles[0].asJsonObject
    }
}