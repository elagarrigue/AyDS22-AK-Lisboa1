package ayds.lisboa.songinfo.otherdetails.model.repository.local.service

import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard

interface CardLocalStorage {
    fun saveArtist(serviceCard: ServiceCard)

    fun getInfo(artist: String): List<ServiceCard>
}