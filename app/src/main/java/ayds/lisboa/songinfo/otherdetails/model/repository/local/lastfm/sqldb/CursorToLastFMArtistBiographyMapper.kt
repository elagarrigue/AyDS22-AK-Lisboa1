package ayds.lisboa.songinfo.otherdetails.model.repository.local.lastfm.sqldb

import ayds.lisboa.songinfo.otherdetails.model.entities.LastFMArtistBiography
import android.database.Cursor
import java.sql.SQLException

interface CursorToLastFMArtistBiographyMapper{

    fun map(cursor: Cursor): LastFMArtistBiography?
}

internal class CursorToLastFMArtistBiographyMapperImpl : CursorToLastFMArtistBiographyMapper {

    override fun map(cursor: Cursor): LastFMArtistBiography? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFMArtistBiography(
                        artist = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        biography = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        url = getString(getColumnIndexOrThrow(SOURCE_COLUMN)),
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }