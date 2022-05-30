package ayds.lisboa.songinfo.otherdetails.model.repository.external.service

import ayds.lisboa.songinfo.otherdetails.model.entities.Card

interface Broker{
    fun getService (name : String) :  List<Card>
}

internal class BrokerImpl(
    private val proxyLastFM : ProxyLastFM,
    private val proxyNewYorkTimes: ProxyNewYorkTimes,
    private val proxyWikipedia: ProxyWikipedia

) : Broker {

    override fun getService (name : String) : List<Card> {
        var cardList : MutableList<Card> = mutableListOf()
        cardList.add(proxyLastFM.getCard(name))
        cardList.add(proxyNewYorkTimes.getCard(name))
       // cardList.add(proxyWikipedia.getCard(name)) //todo cuando este implementado wikipedia

        return cardList
    }
}