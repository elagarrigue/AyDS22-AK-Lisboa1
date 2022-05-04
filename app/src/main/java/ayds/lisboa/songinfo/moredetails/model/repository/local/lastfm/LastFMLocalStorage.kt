package ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm

import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography

interface LastFMLocalStorage {

    fun saveArtist(artistBiography: LastFMArtistBiography)

    fun getInfo(artist: String): LastFMArtistBiography?
}