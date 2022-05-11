package ayds.lisboa.songinfo.otherdetails.model.repository.local.lastfm.sqldb

const val ARTIST_COLUMN = "artist"
const val SOURCE_COLUMN = "source"
const val INFO_COLUMN = "info"
const val ARTIST_TABLE = "artists"
const val SORT_ORDER = "artist DESC"
const val SELECTION = "artist  = ?"

const val createArtistTableQuery: String =
    "create table $ARTIST_TABLE (" +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN integer)"
