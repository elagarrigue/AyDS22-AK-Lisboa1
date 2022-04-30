package ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb

const val ARTIST_COLUMN = "artist"
const val SOURCE_COLUMN = "source"
const val INFO_COLUMN = "info"
const val ID_COLUMN = "id"
const val ARTIST_TABLE = "artists"
const val SORT_ORDER = "artist DESC"
const val SELECTION = "artist  = ?"

const val createArtistTableQuery: String =
    "create table $ARTIST_TABLE (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN integer)"
