package ayds.lisboa.songinfo.otherdetails.model.repository.external.lastfm

import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography

interface LastFMService {
    fun getArtistBio(artist : String) : LastFMArtistBiography?
}