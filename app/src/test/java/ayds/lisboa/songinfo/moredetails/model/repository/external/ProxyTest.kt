package ayds.lisboa.songinfo.moredetails.model.repository.external

import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import ayds.lisboa.songinfo.otherdetails.model.entities.Source
import ayds.lisboa.songinfo.otherdetails.model.repository.external.ProxyLastFM
import ayds.lisboa.songinfo.otherdetails.model.repository.external.ProxyNewYorkTimes
import ayds.lisboa.songinfo.otherdetails.model.repository.external.ProxyWikipedia
import ayds.lisboa1.lastfm.LastFMArtistBiography
import ayds.lisboa1.lastfm.LastFMService
import ayds.newyork2.newyorkdata.nytimes.NYTimesArtistInfo
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
import ayds.winchester2.wikipedia.ExternalRepository
import ayds.winchester2.wikipedia.WikipediaArticle
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class ProxyTest {

    private val lastFmService : LastFMService = mockk(relaxUnitFun = true)
    private val newYorkTimesService: NYTimesService = mockk(relaxUnitFun = true)
    private val wikipediaService: ExternalRepository = mockk(relaxUnitFun = true)

    private val proxyLastFM : ProxyLastFM by lazy {
        ProxyLastFM(lastFmService)
    }

    private val proxyNewYorkTimes: ProxyNewYorkTimes by lazy{
        ProxyNewYorkTimes(newYorkTimesService)
    }

    private val proxyWikipedia: ProxyWikipedia by lazy{
        ProxyWikipedia(wikipediaService)
    }

    @Test
    fun `ProxyLastFM given an existing artist should return a ServiceCard `(){

        val card = LastFMArtistBiography("artist","biography","articleUrl","logoUrl")

        every{ lastFmService.getArtistBio("artist") } returns card

        val result = proxyLastFM.getCard("artist")
        val expected = ServiceCard("artist", "biography", "articleUrl", Source.LASTFM, "logoUrl", false)

        assertEquals(expected,result)

    }

    @Test
    fun `ProxyLastFM given a non existing artist should return an EmptyCard`(){

        every{lastFmService.getArtistBio("artist")} returns null

        val result = proxyLastFM.getCard("artist")

        assertEquals(EmptyCard,result)
    }

    @Test
    fun `ProxyLastFm given a service exception should return an EmptyCard`(){

        every{lastFmService.getArtistBio("artist")} throws mockk<Exception>()

        val result = proxyLastFM.getCard("artist")

        assertEquals(EmptyCard,result)
    }

    @Test
    fun `ProxyNewYorkTimes given an existing artist should return a ServiceCard `(){

        val card = NYTimesArtistInfo("artistName","artistInfo","artistUrl")

        every{ newYorkTimesService.getArtist("artistName") } returns card

        val result = proxyNewYorkTimes.getCard("artistName")
        val expected = ServiceCard("artistName", "artistInfo", "artistUrl", Source.NEW_YORK_TIMES, card.source_logo_url, false)

        assertEquals(expected,result)

    }

    @Test
    fun `ProxyNewYorkTimes given a non existing artist should return an EmptyCard`(){

        every{newYorkTimesService.getArtist("artistName")} returns null

        val result = proxyNewYorkTimes.getCard("artistName")

        assertEquals(EmptyCard,result)
    }

    @Test
    fun `ProxyNewYorkTimes given a service exception should return an EmptyCard`(){

        every{newYorkTimesService.getArtist("artistName")} throws mockk<Exception>()

        val result = proxyNewYorkTimes.getCard("artistName")

        assertEquals(EmptyCard,result)
    }

    @Test
    fun `ProxyWikipedia given an existing artist should return a ServiceCard `(){

        val card = WikipediaArticle("source","description","sourceLogo")

        every{ wikipediaService.getArtistDescription("artist") } returns card

        val result = proxyWikipedia.getCard("artist")
        val expected = ServiceCard("", "description", "source", Source.WIKIPEDIA, "sourceLogo", false)

        assertEquals(expected,result)

    }

    @Test
    fun `ProxyWikipedia given a non existing artist should return an empty ServiceCard`(){

        val card = WikipediaArticle("","","")

        every{wikipediaService.getArtistDescription("artist")} returns card

        val result = proxyWikipedia.getCard("artist")
        val expected = ServiceCard("","","",Source.WIKIPEDIA,"",false)

        assertEquals(expected,result)
    }

    @Test
    fun `ProxyWikipedia given a service exception should return an EmptyCard`(){

        every{wikipediaService.getArtistDescription("artist")} throws mockk<Exception>()

        val result = proxyWikipedia.getCard("artist")

        assertEquals(EmptyCard,result)
    }

}