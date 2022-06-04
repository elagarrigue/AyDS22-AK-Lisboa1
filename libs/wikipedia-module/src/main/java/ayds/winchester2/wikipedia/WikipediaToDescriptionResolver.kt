package ayds.winchester2.wikipedia


import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response

private const val SNIPPET = "snippet"
private const val SEARCH = "search"
private const val PAGE_ID = "pageid"
private const val QUERY = "query"

private const val URL_LOGO = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
private const val URL_SOURCE = "https://en.wikipedia.org/?curid="

interface WikipediaToDescriptionResolver{
    fun getDescriptionFromExternalData(queryWikipediaSearch : Response<String>): WikipediaArticle
}

internal class WikipediaToDescriptionResolverImpl : WikipediaToDescriptionResolver{

    private fun makeDescription(queryWikipediaSearch: JsonObject): String {
        val snippet = getSnippet(queryWikipediaSearch)
        return getArtistDescription(snippet)
    }

    override fun getDescriptionFromExternalData(queryWikipediaSearch : Response<String>): WikipediaArticle {

        val gson = Gson()
        val jObj = gson.fromJson(queryWikipediaSearch.body(), JsonObject::class.java)

        val query = jObj[QUERY].asJsonObject

        val artistDescription = makeDescription(query)
        val pageUrl = URL_SOURCE + getPageId(query).asString

        return WikipediaArticle(pageUrl,artistDescription,URL_LOGO)
    }

    private fun getSnippet(json: JsonObject) : JsonElement {
        return json[SEARCH].asJsonArray[0].asJsonObject[SNIPPET]
    }

    private fun getArtistDescription(snippet: JsonElement) : String{
        return snippet.asString.replace("\\n", "\n")
    }

    private fun getPageId(json: JsonObject) : JsonElement {
        return json[SEARCH].asJsonArray[0].asJsonObject[PAGE_ID]
    }

}