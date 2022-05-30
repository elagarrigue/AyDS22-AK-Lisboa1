package ayds.lisboa.songinfo.otherdetails.model.repository.external.service

object BrokerInjector { //todo esta bien un injector con los tres proxy?
    private lateinit var broker: Broker

    fun getBroker() : Broker = broker
}