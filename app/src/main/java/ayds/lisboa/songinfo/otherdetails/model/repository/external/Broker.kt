package ayds.lisboa.songinfo.otherdetails.model.repository.external

import ayds.lisboa.songinfo.otherdetails.model.entities.Card
import ayds.lisboa.songinfo.otherdetails.model.entities.EmptyCard

interface Broker{
    fun getCards (name : String) :  List<Card>
}

internal class BrokerImpl(
    private val proxyLastFM : ProxyLastFM,
    private val proxyNewYorkTimes: ProxyNewYorkTimes,
    private val proxyWikipedia: ProxyWikipedia

) : Broker {

    override fun getCards (name : String) : List<Card> {
        var cardList : MutableList<Card> = mutableListOf()
        cardList.add(checkCard(proxyLastFM.getCard(name)))
        cardList.add(checkCard(proxyNewYorkTimes.getCard(name)))
        cardList.add(checkCard(proxyWikipedia.getCard(name)))

        return cardList
    }

    private fun checkCard(card: Card?): Card {
        return card ?: EmptyCard
    }
}