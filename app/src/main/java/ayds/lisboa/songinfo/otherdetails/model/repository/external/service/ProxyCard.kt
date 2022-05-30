package ayds.lisboa.songinfo.otherdetails.model.repository.external.service

import ayds.lisboa.songinfo.otherdetails.model.entities.Card

interface ProxyCard {

    fun getCard (artist: String) : Card

}