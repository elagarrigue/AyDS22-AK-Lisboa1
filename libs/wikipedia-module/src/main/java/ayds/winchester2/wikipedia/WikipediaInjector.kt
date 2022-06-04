package ayds.winchester2.wikipedia

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val URL_RETROFIT = "https://en.wikipedia.org/w/"

object WikipediaInjector {
    private val wikipediaAPIRetrofit = Retrofit.Builder()
        .baseUrl(URL_RETROFIT)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val wikipediaAPI = wikipediaAPIRetrofit.create(WikipediaAPI::class.java)
    private val wikipediaToDescriptionResolver: WikipediaToDescriptionResolver = WikipediaToDescriptionResolverImpl()

    val wikipediaService: ExternalRepository = ExternalRepositoryImpl(
        wikipediaAPI, wikipediaToDescriptionResolver
    )
}