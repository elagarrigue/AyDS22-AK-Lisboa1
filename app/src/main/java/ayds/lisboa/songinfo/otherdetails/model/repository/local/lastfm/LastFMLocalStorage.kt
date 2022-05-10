package ayds.lisboa.songinfo.otherdetails.model.repository.local.lastfm

import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography

interface LastFMLocalStorage {

    fun saveArtist(artistBiography: LastFMArtistBiography)

    fun getInfo(artist: String): LastFMArtistBiography?
}