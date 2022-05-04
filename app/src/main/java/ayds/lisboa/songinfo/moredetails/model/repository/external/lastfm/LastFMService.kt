package ayds.lisboa.songinfo.moredetails.model.repository.external.lastfm

import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography

interface LastFMService {
    fun getArtistBio(artist : String) : LastFMArtistBiography?
}